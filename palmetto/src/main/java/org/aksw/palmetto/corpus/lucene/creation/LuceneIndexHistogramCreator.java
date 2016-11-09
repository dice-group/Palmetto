/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.corpus.lucene.creation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.List;

import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.store.SimpleFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntIntOpenHashMap;

/**
 * A simple class that iterates over a given Lucene index and creates a
 * histogram of the document lengths stored inside a documentLengthField.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 * 
 */
public class LuceneIndexHistogramCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneIndexHistogramCreator.class);

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
			dirReader = DirectoryReader.open(new SimpleFSDirectory(Paths.get(indexPath)));
			List<LeafReaderContext> leaves = dirReader.leaves();
			IndexReader reader;
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
