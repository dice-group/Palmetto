/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.corpus.lucene.creation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.apache.lucene.index.AtomicReader;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

/**
 * A simple class that iterates over a given Lucene index and creates a histogram of the document lengths stored inside
 * a documentLengthField.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class LuceneIndexHistogramCreator {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(LuceneIndexHistogramCreator.class);

    private String docLengthFieldName;

    public LuceneIndexHistogramCreator(String docLengthFieldName) {
        this.docLengthFieldName = docLengthFieldName;
    }

    public void createLuceneIndexHistogram(String indexPath) {
        int histogram[][] = createHistogram(indexPath);
        if (histogram != null) {
            storeHistogram(histogram, indexPath + WindowSupportingLuceneCorpusAdapter.HISTOGRAM_FILE_SUFFIX);
        }
    }

    protected int[][] createHistogram(String indexPath) {
        IntIntOpenHashMap histogram = new IntIntOpenHashMap();
        DirectoryReader dirReader = null;
        try {
            dirReader = DirectoryReader.open(new SimpleFSDirectory(
                    new File(indexPath)));
            List<AtomicReaderContext> leaves = dirReader.leaves();
            AtomicReader reader;
            int documentCount = 0;
            IndexableField field;
            Number number;
            for (int i = 0; i < leaves.size(); ++i) {
                reader = leaves.get(i).reader();
                for (int d = 0; d < reader.maxDoc(); ++d) {
                    field = reader.document(d).getField(docLengthFieldName);
                    if (field != null) {
                        number = field.numericValue().intValue();
                        if (number != null) {
                            ++documentCount;
                            histogram.putOrAdd(number.intValue(), 1, 1);
                        }
                    }
                }
            }
            LOGGER.info("Saw " + documentCount + " documents.");
        } catch (IOException e) {
            LOGGER.error("Error while reading from index. Returning null.");
            return null;
        } finally {
            if (dirReader != null) {
                try {
                    dirReader.close();
                } catch (IOException e) {
                }
            }
        }
        int result[][] = new int[histogram.size()][2];
        int posInResult = 0;
        long completeSum = 0;
        for (int i = 0; i < histogram.keys.length; ++i) {
            if (histogram.allocated[i]) {
                result[posInResult][0] = histogram.keys[i];
                result[posInResult][1] = histogram.values[i];
                completeSum += result[posInResult][0] * result[posInResult][1];
                ++posInResult;
            }
        }
        LOGGER.info("Counted " + completeSum + " tokens.");
        return result;
    }

    public void storeHistogram(int histogram[][], String filename) {
        File file = new File(filename);
        if ((file.getParentFile() != null) && (!file.getParentFile().exists())) {
            file.getParentFile().mkdirs();
        }
        FileOutputStream fout = null;
        ObjectOutputStream oout = null;
        try {
            fout = new FileOutputStream(filename);
            oout = new ObjectOutputStream(fout);
            oout.writeObject(histogram);
        } catch (Exception e) {
            LOGGER.error("Couldn't store histogram.");
        } finally {
            try {
                oout.close();
            } catch (Exception e) {
                // nothing to do
            }
            try {
                fout.close();
            } catch (Exception e) {
                // nothing to do
            }
        }
    }
}
