package org.aksw.palmetto.prob;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.subsets.AnyAny;
import org.aksw.palmetto.subsets.OneAll;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.subsets.SubsetDefinition;
import org.aksw.palmetto.subsets.SubsetProbabilities;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.carrotsearch.hppc.IntOpenHashSet;
import com.carrotsearch.hppc.ObjectObjectOpenHashMap;

@RunWith(Parameterized.class)
public class BooleanDocumentProbabilitySupplierTest implements BooleanDocumentSupportingAdapter {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word0 1 1 1
                         * 
                         * word1 0 1 1
                         * 
                         * word2 0 0 1
                         */
                        { new OneAll(), new int[][] { { 0, 1, 2 }, { 1, 2 }, { 2 } }, 3, 1,
                                new double[] { 1.0, 2.0 / 3.0, 1.0 / 3.0 },
                                new double[][] { { 1.0 }, { 1.0 }, { 0.5 } } },
                        /*
                         * word0 1 1 1
                         * 
                         * word1 0 1 1
                         * 
                         * word2 0 0 1
                         * 
                         * with min freq = 2
                         */
                        { new OneAll(), new int[][] { { 0, 1, 2 }, { 1, 2 }, { 2 } }, 3, 2,
                                new double[] { 1.0, 2.0 / 3.0, 0.0 },
                                new double[][] { { 0.0 }, { 0.0 }, { 0.0 } } },

                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * C_d,anyany= 1/12 * (
                         * (P(w_1|w_2)-P(w_1)) + (P(w_1|w_3)-P(w_1)) + (P(w_1|w_2,w_3)-P(w_1)) +
                         * (P(w_2|w_1)-P(w_2)) + (P(w_2|w_3)-P(w_2)) + (P(w_2|w_1,w_3)-P(w_2)) +
                         * (P(w_3|w_1)-P(w_3)) + (P(w_3|w_2)-P(w_3)) + (P(w_3|w_1,w_2)-P(w_3)) +
                         * (P(w_1,w_2|w_3)-P(w_1,w_2)) +
                         * (P(w_1,w_3|w_2)-P(w_1,w_3)) +
                         * (P(w_2,w_3|w_1)-P(w_2,w_3))) = 1/12 * ((1 - 0.25) +
                         * (0.5 - 0.25) + (0.5 - 0.25) + (1 - 0.5) + (1 - 0.5) +
                         * (0.5 - 0.5) + (1 - 0.5) + (1 - 0.5) + (0.5 - 0.5) +
                         * (0.5 - 0.25) + (0.5 - 0.25) + (1 - 0.25)) = 1/12 *
                         * (0.75 + 0.25 + 0.25 + 0.5 + 0.5 + 0 + 0.5 + 0.5 + 0 +
                         * 0.25 + 0.25 + 0.75) = 4.5/12
                         */
                        { new AnyAny(), new int[][] { { 3 }, { 1, 3 }, { 2, 3 } }, 4, 1,
                                new double[] { 0.25, 0.5, 0.25, 0.5, 0.25, 0.25 },
                                new double[][] { { 0.5, 0.5, 1.0 },
                                { 1.0, 0.5, 1.0 }, { 0.5 }, { 1.0, 0.5, 1.0 },
                                { 0.5 }, { 1.0 } } } });
    }

    private SubsetCreator creator;
    private int wordDocuments[][];
    private int numberOfDocuments;
    private int minFrequency;
    private double expectedSegmentProbs[];
    private double expectedCondProbs[][];

    public BooleanDocumentProbabilitySupplierTest(SubsetCreator creator, int[][] wordDocuments, int numberOfDocuments,
            int minFrequency, double[] expectedSegmentProbs, double[][] expectedCondProbs) {
        this.creator = creator;
        this.wordDocuments = wordDocuments;
        this.numberOfDocuments = numberOfDocuments;
        this.minFrequency = minFrequency;
        this.expectedSegmentProbs = expectedSegmentProbs;
        this.expectedCondProbs = expectedCondProbs;
    }

    @Test
    public void test() {
        String words[] = new String[wordDocuments.length];
        for (int i = 0; i < words.length; i++) {
            words[i] = Integer.toString(i);
        }

        BooleanDocumentProbabilitySupplier probSupplier = BooleanDocumentProbabilitySupplier.create(this);
        probSupplier.setMinFrequency(minFrequency);
        SubsetProbabilities subsetProbs[] = probSupplier.getProbabilities(new String[][] { words },
                new SubsetDefinition[] { creator.getSubsetDefinition(words.length) });

        double segmentProbs[] = subsetProbs[0].segmentProbabilities;
        Assert.assertArrayEquals(expectedSegmentProbs, segmentProbs, DOUBLE_PRECISION_DELTA);
        double condProbs[][] = subsetProbs[0].conditionProbabilities;
        for (int i = 0; i < condProbs.length; i++) {
            Assert.assertArrayEquals("Conditional probabilities of segment " + i + " are not correct.",
                    expectedCondProbs[i], condProbs[i], DOUBLE_PRECISION_DELTA);
        }
    }

    public void getDocumentsWithWords(ObjectObjectOpenHashMap<String, IntOpenHashSet> wordDocMapping) {
        Object keys[] = (Object[]) wordDocMapping.keys;
        Object values[] = (Object[]) wordDocMapping.values;
        for (int i = 0; i < wordDocMapping.allocated.length; ++i) {
            if (wordDocMapping.allocated[i]) {
                ((IntOpenHashSet) values[i]).add(wordDocuments[Integer.parseInt((String) keys[i])]);
            }
        }
    }

    public int getNumberOfDocuments() {
        return numberOfDocuments;
    }
}
