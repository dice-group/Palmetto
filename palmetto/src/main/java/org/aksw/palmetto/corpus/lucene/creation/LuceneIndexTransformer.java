/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.corpus.lucene.creation;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.SimpleAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class creates a simpler boolean document index based on a position
 * storing index. Most users won't find this class helpful since it has only
 * been created for a special situation, i.e., a user has a (large) position
 * storing index and want to transform it into a faster boolean document index.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class LuceneIndexTransformer extends AbstractLuceneIndexCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(LuceneIndexTransformer.class);

    /**
     * Constructor.
     * 
     * @param fieldName The name of the field in which the document texts are
     *                  stored.
     */
    public LuceneIndexTransformer(String fieldName) {
        super(fieldName);
    }

    /**
     * Constructor.
     * 
     * @param textFieldName  The name of the field in which the document texts are
     *                       stored.
     * @param commitInterval The interval in which changes are committed to the
     *                       index.
     */
    public LuceneIndexTransformer(String textFieldName, int commitInterval) {
        super(textFieldName, commitInterval);
    }

    /**
     * Creates the index.
     * 
     * @param indexPath   The path to the director in which the Lucene index will be
     *                    created
     * @param docIterator Iterator that iterates over the document texts.
     * @return true if the creation was successful, else false.
     */
    public boolean createIndex(File posIndexPath, File booleanIndexPath, Set<String> whiteList) {
        LOGGER.info("Starting index creation...");
        IndexWriter writer = null;
        booleanIndexPath.mkdirs();
        Analyzer analyzer = new SimpleAnalyzer(true);
        try {
            IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
            config.setOpenMode(OpenMode.CREATE);

            FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
            fieldType.setIndexed(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_ONLY);
            // fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
            fieldType.freeze();

            writer = new IndexWriter(FSDirectory.open(booleanIndexPath), config);
            int count = 0;

            DirectoryReader dirReader = DirectoryReader.open(new NIOFSDirectory(posIndexPath));
            TermsEnum tEnum = null;
            BytesRef bytes;
            StringBuilder builder = new StringBuilder();
            int numberOfDocuments = dirReader.numDocs();
            String term;
            for (int i = 0; i < numberOfDocuments; i++) {
                builder.delete(0, builder.length());
                Terms terms = dirReader.getTermVector(i, textFieldName);
                tEnum = terms.iterator(tEnum);
                // System.out.println(tEnum.next().bytes);
                bytes = tEnum.next();
                while (bytes != null) {
                    term = new String(bytes.bytes, 0, bytes.length);
                    if ((whiteList == null) || (whiteList.contains(term))) {
                        builder.append(' ');
                        builder.append(new String(bytes.bytes, 0, bytes.length));
                    }
                    bytes = tEnum.next();
                }
                writer.addDocument(toLuceneDocument(analyzer, builder.toString(), fieldType));
                ++count;
                if (count >= commitInterval) {
                    System.out.println("commiting after id " + i);
                    writer.commit();
                    System.gc();
                    count = 0;
                }
            }

            LOGGER.info("Finished index creation.");
        } catch (IOException e) {
            LOGGER.error("Error while creating Index. Aborting.", e);
            return false;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    public Set<String> createWhiteList(File bpIndexPath) throws IOException {
        Set<String> whiteList = new HashSet<String>();
        DirectoryReader dirReader = DirectoryReader.open(new NIOFSDirectory(bpIndexPath));
        TermsEnum tEnum = null;
        BytesRef bytes;
        AtomicReader reader;
        for (AtomicReaderContext context : dirReader.leaves()) {
            reader = context.reader();
            Terms terms = reader.terms(textFieldName);
            tEnum = terms.iterator(tEnum);
            bytes = tEnum.next();
            while (bytes != null) {
                whiteList.add(new String(bytes.bytes, 0, bytes.length));
                bytes = tEnum.next();
            }
        }
        System.out.println("Created whitelist with " + whiteList.size() + " terms.");
        return whiteList;
    }

    public static void main(String[] args) throws IOException {
        File posIndexPath = new File("/home/micha/data/wikipedia_bd");
        File bdIndexPath = new File("/home/micha/Downloads/temp/Wikipedia_bd");
        File bpIndexPath = new File("/home/micha/Downloads/temp/Wikipedia_bp");

        LuceneIndexTransformer transformer = new LuceneIndexTransformer(Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        transformer.createIndex(posIndexPath, bdIndexPath, transformer.createWhiteList(bpIndexPath));
    }
}
