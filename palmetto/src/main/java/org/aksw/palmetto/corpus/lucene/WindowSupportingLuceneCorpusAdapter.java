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
package org.aksw.palmetto.corpus.lucene;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Paths;
import java.util.List;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class WindowSupportingLuceneCorpusAdapter extends LuceneCorpusAdapter implements WindowSupportingAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(WindowSupportingLuceneCorpusAdapter.class);

	public static final String HISTOGRAM_FILE_SUFFIX = ".histogram";

	public static WindowSupportingLuceneCorpusAdapter create(String indexPath, String textFieldName,
			String docLengthFieldName) throws CorruptIndexException, IOException {
		DirectoryReader dirReader = DirectoryReader.open(new NIOFSDirectory(Paths.get(indexPath)));
		List<LeafReaderContext> leaves = dirReader.leaves();
		LeafReader reader[] = new LeafReader[leaves.size()];
		LeafReaderContext contexts[] = new LeafReaderContext[leaves.size()];
		for (int i = 0; i < reader.length; i++) {
			contexts[i] = leaves.get(i);
			reader[i] = contexts[i].reader();
		}
		int histogram[][] = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = new FileInputStream(indexPath + HISTOGRAM_FILE_SUFFIX);
			ois = new ObjectInputStream(fis);
			histogram = (int[][]) ois.readObject();
		} catch (Exception e) {
			LOGGER.error("Couldn't read histogram file. Returning null.", e);
		} finally {
			try {
				ois.close();
				fis.close();
			} catch (Exception e) {
				// nothing to do
			}
		}
		if (histogram == null) {
			return null;
		}

		return new WindowSupportingLuceneCorpusAdapter(dirReader, reader, contexts, textFieldName, docLengthFieldName,
				histogram);
	}

	protected int histogram[][];
	protected String docLengthFieldName;

	protected WindowSupportingLuceneCorpusAdapter(DirectoryReader dirReader, LeafReader[] reader,
			LeafReaderContext contexts[], String textFieldName, String docLengthFieldName, int histogram[][]) {
		super(dirReader, reader, contexts, textFieldName);
		this.histogram = histogram;
		this.docLengthFieldName = docLengthFieldName;
	}

	@Override
	public int[][] getDocumentSizeHistogram() {
		return histogram;
	}

	@Override
	public IntObjectOpenHashMap<IntArrayList[]> requestWordPositionsInDocuments(String[] words,
			IntIntOpenHashMap docLengths) {
		IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = new IntObjectOpenHashMap<IntArrayList[]>();
		for (int i = 0; i < words.length; ++i) {
			requestDocumentsWithWord(words[i], positionsInDocs, docLengths, i, words.length);
		}
		return positionsInDocs;
	}

	protected void requestDocumentsWithWord(String word, IntObjectOpenHashMap<IntArrayList[]> positionsInDocs,
			IntIntOpenHashMap docLengths, int wordId, int numberOfWords) {
		PostingsEnum docPosEnum = null;
		Term term = new Term(fieldName, word);
		int localDocId, globalDocId, baseDocId;
		IntArrayList positions[];
		try {
			for (int i = 0; i < reader.length; i++) {
				LeafReader theLeafReader = (LeafReader) reader[i];
				docPosEnum = theLeafReader.postings(term, PostingsEnum.POSITIONS);
				baseDocId = ((LeafReaderContext) (contexts[i])).docBase;
				if (docPosEnum != null) {
					while (docPosEnum.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
						localDocId = docPosEnum.docID();
						globalDocId = localDocId + baseDocId;
						// if this is the first word and we found a new document
						if (!positionsInDocs.containsKey(globalDocId)) {
							positions = new IntArrayList[numberOfWords];
							positionsInDocs.put(globalDocId, positions);
						} else {
							positions = positionsInDocs.get(globalDocId);
						}
						if (positions[wordId] == null) {
							positions[wordId] = new IntArrayList();
						}
						// Go through the positions inside this document
						for (int p = 0; p < docPosEnum.freq(); ++p) {
							positions[wordId].add(docPosEnum.nextPosition());
						}
						if (!docLengths.containsKey(globalDocId)) {
							// Get the length of the document
							docLengths.put(globalDocId, reader[i].document(localDocId).getField(docLengthFieldName)
									.numericValue().intValue());
						}
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while requesting documents for word \"" + word + "\".", e);
		}
	}

}
