package org.aksw.palmetto.corpus.lucene;

import java.io.IOException;

import junit.framework.Assert;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.prob.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.apache.lucene.index.CorruptIndexException;
import org.junit.Test;

public class LuceneCorpusAdapterForSlidingWindowsTest {

    private static final String TEST_INDEX_PATH = "src/test/resources/test_bd";

    @Test
    public void test() throws CorruptIndexException, IOException {
        LuceneCorpusAdapterForSlidingWindows corpusAdapter = LuceneCorpusAdapterForSlidingWindows.create(
                TEST_INDEX_PATH, Palmetto.DEFAULT_INDEX_FIELD_NAME);
        BooleanSlidingWindowFrequencyDeterminer determiner = new BooleanSlidingWindowFrequencyDeterminer(corpusAdapter,
                10);
        int counts[] = determiner.determineCounts(new String[][] { { "new", "york" } },
                new SubsetDefinition[] { new SubsetDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        // New count
        Assert.assertTrue(counts[1] > 0);
        // York count
        Assert.assertTrue(counts[2] > 0);
        // New York count
        Assert.assertTrue(counts[3] > 0);
        int secondCounts[] = determiner.determineCounts(new String[][] { { "york", "new" } },
                new SubsetDefinition[] { new SubsetDefinition(
                        new int[0], new int[0][0], null) })[0].counts;
        Assert.assertEquals(counts[1], secondCounts[2]);
        Assert.assertEquals(counts[2], secondCounts[1]);
        Assert.assertEquals(counts[3], secondCounts[3]);
    }
}
