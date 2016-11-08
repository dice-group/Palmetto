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

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexReaderContext;
import org.apache.lucene.index.LeafReader;
import org.apache.lucene.index.LeafReaderContext;
import org.apache.lucene.index.PostingsEnum;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.NIOFSDirectory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

/**
 * This class can make usage of a given Lucene index as corpus.
 * 
 * @author m.roeder
 * 
 */
public class LuceneCorpusAdapter implements BooleanDocumentSupportingAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LuceneCorpusAdapter.class);

	protected String fieldName;
	protected DirectoryReader dirReader;
	protected IndexReader reader[];
	protected IndexReaderContext contexts[];

	/**
	 * Creates a corpus adapter which uses the Lucene index with the given path
	 * and searches on the field with the given field name.
	 * 
	 * @param indexPath
	 * @param fieldName
	 * @return
	 * @throws CorruptIndexException
	 * @throws IOException
	 */
	public static LuceneCorpusAdapter create(String indexPath, String fieldName)
			throws CorruptIndexException, IOException {
		DirectoryReader dirReader = DirectoryReader.open(new NIOFSDirectory(Paths.get(indexPath)));
		List<LeafReaderContext> leaves = dirReader.leaves();
		IndexReader reader[] = new LeafReader[leaves.size()];
		IndexReaderContext contexts[] = new LeafReaderContext[leaves.size()];
		for (int i = 0; i < reader.length; i++) {
			contexts[i] = leaves.get(i);
			reader[i] = contexts[i].reader();
		}
		return new LuceneCorpusAdapter(dirReader, reader, contexts, fieldName);
	}

	protected LuceneCorpusAdapter(DirectoryReader dirReader, IndexReader reader[], IndexReaderContext contexts[],
			String fieldName) {
		this.dirReader = dirReader;
		this.reader = reader;
		this.contexts = contexts;
		this.fieldName = fieldName;
	}

	private void requestDocumentsWithWordAsSet(String word, IntOpenHashSet documents) {
		PostingsEnum docs = null;
		Term term = new Term(fieldName, word);
		try {
			int baseDocId;
			for (int i = 0; i < reader.length; i++) {
				LeafReader theLeafReader = (LeafReader) reader[i];
				docs = theLeafReader.postings(term);
				baseDocId = ((LeafReaderContext) (contexts[i])).docBase;
				if (docs != null) {
					while (docs.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
						documents.add(baseDocId + docs.docID());
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while requesting documents for word \"" + word + "\".", e);
		}
	}

	/**
	 * Closes the Lucene index.
	 */
	public void close() {
		for (int i = 0; i < reader.length; i++) {
			try {
				reader[i].close();
			} catch (IOException e) {
				// nothing to do
			}
		}
	}

	public int getNumberOfDocuments() {
		try {
			int numDocs = dirReader.getDocCount(fieldName);
			if (numDocs < 0) {
				LOGGER.warn("Coudln't get the number of documents with the indexed field \"" + fieldName
						+ "\". Using the number of all documents in the index instead.");
			}
		} catch (IOException e) {
			LOGGER.warn("Coudln't get the number of documents with the indexed field \"" + fieldName
					+ "\". Using the number of all documents in the index instead.", e);
		}
		return dirReader.numDocs();
	}

	public void getDocumentsWithWordsAsSet(ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping) {
		Object keys[] = (Object[]) wordDocMapping.keys;
		Object values[] = (Object[]) wordDocMapping.values;
		for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
			if (wordDocMapping.allocated[i]) {
				requestDocumentsWithWordAsSet((String) keys[i], (IntOpenHashSet) values[i]);
			}
		}
	}

	@Override
	public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping) {
		Object keys[] = (Object[]) wordDocMapping.keys;
		Object values[] = (Object[]) wordDocMapping.values;
		for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
			if (wordDocMapping.allocated[i]) {
				requestDocumentsWithWord((String) keys[i], (IntArrayList) values[i]);
			}
		}
	}

	private void requestDocumentsWithWord(String word, IntArrayList documents) {
		PostingsEnum docs = null;
		Term term = new Term(fieldName, word);
		try {
			int baseDocId;
			for (int i = 0; i < reader.length; i++) {
				LeafReader theLeafReader = (LeafReader) reader[i];
				docs = theLeafReader.postings(term);
				baseDocId = ((LeafReaderContext) (contexts[i])).docBase;
				if (docs != null) {
					while (docs.nextDoc() != PostingsEnum.NO_MORE_DOCS) {
						documents.add(docs.docID() + baseDocId);
					}
				}
			}
		} catch (IOException e) {
			LOGGER.error("Error while requesting documents for word \"" + word + "\".", e);
		}
	}
}
