/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.corpus.lucene;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import org.aksw.palmetto.io.DocumentTextSupplier;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.FieldInfo.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IndexCreator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(IndexCreator.class);

    private static final Version version = Version.LUCENE_44;

    private String fieldName;

    public IndexCreator(String fieldName) {
        this.fieldName = fieldName;
    }

    public boolean createIndex(File indexPath, DocumentTextSupplier supplier) {
        LOGGER.info("Starting index creation...");
        IndexWriter writer = null;
        indexPath.mkdirs();
        Analyzer analyzer = new SimpleAnalyzer(true);
        try {
            IndexWriterConfig config = new IndexWriterConfig(version, analyzer);
            config.setOpenMode(OpenMode.CREATE);

            FieldType fieldType = new FieldType(TextField.TYPE_NOT_STORED);
            fieldType.setIndexed(true);
            fieldType.setIndexOptions(IndexOptions.DOCS_AND_FREQS);
            fieldType.freeze();

            writer = new IndexWriter(FSDirectory.open(indexPath), config);
            String text = supplier.getNextDocumentText();
            while (text != null) {
                writer.addDocument(toLuceneDocument(analyzer,
                        text, fieldType));
                text = supplier.getNextDocumentText();
            }
            // LOGGER.info("Optimizing...");
            // writer.optimize();
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

    private Document toLuceneDocument(Analyzer analyzer, String text, FieldType fieldType)
            throws IOException {
        Document document = new Document();
        document.add(new Field(fieldName, analyzer.tokenStream(fieldName,
                new StringReader(text.toString())), fieldType));
        return document;
    }
}
