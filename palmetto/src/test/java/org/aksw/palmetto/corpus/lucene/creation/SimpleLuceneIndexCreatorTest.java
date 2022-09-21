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
import java.util.Arrays;
import java.util.Iterator;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.Assert;
import org.junit.Test;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

public class SimpleLuceneIndexCreatorTest {

    public static String DOCUMENTS[] = { "This is a test document.", "This is another test document.",
            "This is a third one." };
    public static String TEST_WORDS[] = { "is", "document", "dog" };
    public static int EXPECTED_DOC_COUNTS[] = new int[] { 3, 2, 0 };

    @Test
    public void test() throws CorruptIndexException, IOException {
        File indexDir = createTempDirectory();
        Iterator<String> docIterator = Arrays.asList(DOCUMENTS).iterator();
        // create the index
        SimpleLuceneIndexCreator creator = new SimpleLuceneIndexCreator(Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        Assert.assertTrue(creator.createIndex(indexDir, docIterator));

        // test the created index
        // create an adapter
        LuceneCorpusAdapter adapter = LuceneCorpusAdapter.create(indexDir.getAbsolutePath(),
                Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        // query the test words
        ObjectObjectOpenHashMap<String, IntArrayList> wordDocMapping = new ObjectObjectOpenHashMap<String, IntArrayList>();
        for (int i = 0; i < TEST_WORDS.length; ++i) {
            wordDocMapping.put(TEST_WORDS[i], new IntArrayList());
        }
        adapter.getDocumentsWithWords(wordDocMapping);
        // compare the result with the expected counts
        int retrievedDocs;
        for (int i = 0; i < TEST_WORDS.length; ++i) {
            retrievedDocs = wordDocMapping.get(TEST_WORDS[i]).elementsCount;
            Assert.assertEquals("Expected " + EXPECTED_DOC_COUNTS[i] + " documents containing the word \""
                    + TEST_WORDS[i] + "\", but got " + retrievedDocs + " documents form the index.",
                    EXPECTED_DOC_COUNTS[i], retrievedDocs);
        }
    }

    public static File createTempDirectory()
            throws IOException {
        File temp = File.createTempFile("temp_index", Long.toString(System.nanoTime()));
        if (!(temp.delete())) {
            return null;
        }
        if (!(temp.mkdir())) {
            return null;
        }
        return temp;
    }

}
