package org.aksw.palmetto.prob;

import java.util.Arrays;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class BooleanDocumentProbabilitySupplier extends
        AbstractProbabilitySupplier {

    public static BooleanDocumentProbabilitySupplier create(
            CorpusAdapter adapter) {
        BooleanDocumentFrequencyDeterminer determiner = createFrequencyDeterminer(adapter);
        if (determiner != null) {
            return new BooleanDocumentProbabilitySupplier(determiner);
        }
        return null;
    }

    protected static BooleanDocumentFrequencyDeterminer createFrequencyDeterminer(
            CorpusAdapter adapter) {
        if (adapter instanceof BooleanDocumentSupportingAdapter) {
            return new BooleanDocumentFrequencyDeterminer(
                    (BooleanDocumentSupportingAdapter) adapter);
        }
        return null;
    }

    private int numberOfDocuments;

    protected BooleanDocumentProbabilitySupplier(
            BooleanDocumentFrequencyDeterminer freqDeterminer) {
        super(freqDeterminer);
        /*
         * FIXME move this to a better place. Not all frequency determiner will
         * know the number of documents at this point.
         */
        numberOfDocuments = freqDeterminer.getNumberOfDocuments();
    }

    @Override
    protected SubsetProbabilities getProbabilities(CountedSubsets countedSubsets) {
        double segmentProbabilities[] = new double[countedSubsets.segments.length];
        double conditionProbabilities[][] = new double[countedSubsets.conditions.length][];
        double numberOfDocuments = this.numberOfDocuments;
        double jointCounts;
        for (int i = 0; i < segmentProbabilities.length; ++i) {
            conditionProbabilities[i] = new double[countedSubsets.conditions[i].length];
            // if this segment does not fulfill the minimum frequency condition set it to 0
            if (countedSubsets.counts[countedSubsets.segments[i]] >= this.minFrequency) {
                segmentProbabilities[i] = countedSubsets.counts[countedSubsets.segments[i]]
                        / numberOfDocuments;
                for (int j = 0; j < conditionProbabilities[i].length; ++j) {
                    jointCounts = countedSubsets.counts[countedSubsets.segments[i]
                            | countedSubsets.conditions[i][j]];
                    // if the set joining the segment and the condition does not fulfill the minimum frequency condition
                    // set the conditional probability to 0
                    if (countedSubsets.counts[countedSubsets.conditions[i][j]] >= this.minFrequency) {
                        conditionProbabilities[i][j] = jointCounts
                                / (double) countedSubsets.counts[countedSubsets.conditions[i][j]];
                    } else {
                        conditionProbabilities[i][j] = 0;
                    }
                }
            } else {
                segmentProbabilities[i] = 0;
                // All its conditions would be set to 0, too
                Arrays.fill(conditionProbabilities[i], 0);
            }
        }
        return new SubsetProbabilities(segmentProbabilities,
                conditionProbabilities);
    }
}
