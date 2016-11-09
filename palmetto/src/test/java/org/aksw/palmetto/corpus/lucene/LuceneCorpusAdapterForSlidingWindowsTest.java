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

import junit.framework.Assert;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.Ignore;
import org.junit.Test;

public class LuceneCorpusAdapterForSlidingWindowsTest {

    private static final String TEST_INDEX_PATH = "src/test/resources/test_bd";

    @Ignore
    @Test
    public void test() throws CorruptIndexException, IOException {
        WindowSupportingLuceneCorpusAdapter corpusAdapter = WindowSupportingLuceneCorpusAdapter.create(
                TEST_INDEX_PATH, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME,
                Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
        BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter,
                10);
        int counts[] = determiner.determineCounts(new String[][] { { "new", "york" } },
                new SegmentationDefinition[] { new SegmentationDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        // New count
        Assert.assertTrue(counts[1] > 0);
        // York count
        Assert.assertTrue(counts[2] > 0);
        // New York count
        Assert.assertTrue(counts[3] > 0);
        int secondCounts[] = determiner.determineCounts(new String[][] { { "york", "new" } },
                new SegmentationDefinition[] { new SegmentationDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        Assert.assertEquals(counts[1], secondCounts[2]);
        Assert.assertEquals(counts[2], secondCounts[1]);
        Assert.assertEquals(counts[3], secondCounts[3]);
    }
}
