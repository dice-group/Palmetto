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
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.Assert;
import org.junit.Test;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * This test is a copy of the {@link PositionStoringLuceneIndexCreatorTest} that
 * is adapted to a test case in which the search contains an n-gram comprising
 * more than 1 word.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class WordNGramTest {

    public static IndexableDocument DOCUMENTS[] = { new IndexableDocument("This is a test document.", 5),
            new IndexableDocument("This is another test document.", 5), new IndexableDocument("This is the test.", 4) };
    public static int WINDOW_SIZE = 4;
    public static String TEST_WORDS[] = { "is", "test_document", "dog" };
    // expected positions of the words inside the documents (negativ number
    // means that the word is not inside the
    // document
    public static int EXPECTED_WORD_POSITIONS[][] = new int[][] { { 1, 4, -1 }, { 1, 4, -1 }, { 1, -1, -1 } };
    // counts for windows of size 4 containing { ALWAYS 0, "is", "document",
    // "is"+"document", "dog", "is"+"dog", "document"+"dog",
    // "is"+"document"+"dog"}
    public static int EXPECTED_COUNTS[] = new int[] { 0, 5, 2, 2, 0, 0, 0, 0 };

    @Test
    public void test() throws CorruptIndexException, IOException {
        File indexDir = new File(
                FileUtils.getTempDirectoryPath() + File.separator + "temp_index" + Long.toString(System.nanoTime()));
        Assert.assertTrue(indexDir.mkdir());
        Iterator<IndexableDocument> docIterator = Arrays.asList(DOCUMENTS).iterator();
        // create the index
        PositionStoringLuceneIndexCreator creator = new PositionStoringLuceneIndexCreator(
                Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
        Assert.assertTrue(creator.createIndex(indexDir, docIterator));
        LuceneIndexHistogramCreator hCreator = new LuceneIndexHistogramCreator(
                Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
        hCreator.createLuceneIndexHistogram(indexDir.getAbsolutePath());

        // test the created index
        // create an adapter
        WindowSupportingLuceneCorpusAdapter adapter = null;
        try {
            adapter = WindowSupportingLuceneCorpusAdapter.create(indexDir.getAbsolutePath(),
                    Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
            // query the test words
            IntIntOpenHashMap docLengths = new IntIntOpenHashMap();
            IntObjectOpenHashMap<IntArrayList[]> wordPositions = adapter.requestWordPositionsInDocuments(TEST_WORDS,
                    docLengths);
            // compare the result with the expected counts
            int positionInDoc;
            IntArrayList[] positionsInDocs;
            for (int i = 0; i < EXPECTED_WORD_POSITIONS.length; ++i) {
                positionsInDocs = wordPositions.get(i);
                for (int j = 0; j < positionsInDocs.length; ++j) {
                    if (EXPECTED_WORD_POSITIONS[i][j] < 0) {
                        Assert.assertNull("Expected null because the word \"" + TEST_WORDS[j]
                                + "\" shouldn't be found inside document " + i + ". But got a position list instead.",
                                positionsInDocs[j]);
                    } else {
                        Assert.assertEquals(1, positionsInDocs[j].elementsCount);
                        positionInDoc = positionsInDocs[j].buffer[0];
                        Assert.assertEquals("Expected the word \"" + TEST_WORDS[j] + "\" in document " + i
                                + " at position " + EXPECTED_WORD_POSITIONS[i][j] + " but got position " + positionInDoc
                                + " form the index.", EXPECTED_WORD_POSITIONS[i][j], positionInDoc);
                    }
                }
            }

            // test the window based counting
            BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(adapter,
                    WINDOW_SIZE);
            CountedSubsets subsets = determiner.determineCounts(new String[][] { TEST_WORDS },
                    new SegmentationDefinition[] { new SegmentationDefinition(new int[0], new int[0][0], null) })[0];
            Assert.assertArrayEquals(EXPECTED_COUNTS, subsets.counts);
        } finally {
            if (adapter != null) {
                adapter.close();
            }
        }
    }

}
