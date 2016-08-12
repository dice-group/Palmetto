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
package org.aksw.palmetto.prob.bd;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.AbstractProbabilitySupplier;

public class BooleanDocumentProbabilitySupplier extends AbstractProbabilitySupplier {

    private static final String DEFAULT_PROB_MODEL_NAME = "bd";

    public static BooleanDocumentProbabilitySupplier create(CorpusAdapter adapter) {
        return create(adapter, DEFAULT_PROB_MODEL_NAME);
    }

    public static BooleanDocumentProbabilitySupplier create(CorpusAdapter adapter, String probModelName) {
        return create(adapter, probModelName, false);
    }

    public static BooleanDocumentProbabilitySupplier create(CorpusAdapter adapter, String probModelName,
            boolean corpusIsLarge) {
        BooleanDocumentFrequencyDeterminer determiner = createFrequencyDeterminer(adapter, corpusIsLarge);
        if (determiner != null) {
            return new BooleanDocumentProbabilitySupplier(determiner, probModelName);
        }
        return null;
    }

    protected static BooleanDocumentFrequencyDeterminer createFrequencyDeterminer(CorpusAdapter adapter,
            boolean corpusIsLarge) {
        if (adapter instanceof BooleanDocumentSupportingAdapter) {
            return (corpusIsLarge ? new ListBasedBooleanDocumentFrequencyDeterminer(
                    (BooleanDocumentSupportingAdapter) adapter) : new BitSetBasedBooleanDocumentFrequencyDeterminer(
                    (BooleanDocumentSupportingAdapter) adapter));
        }
        return null;
    }

    private int numberOfDocuments;
    private String probModelName;

    protected BooleanDocumentProbabilitySupplier(BooleanDocumentFrequencyDeterminer freqDeterminer,
            String probModelName) {
        super(freqDeterminer);
        this.probModelName = probModelName;
        /*
         * FIXME move this to a better place. Not all frequency determiner will
         * know the number of documents at this point.
         */
        numberOfDocuments = freqDeterminer.getNumberOfDocuments();
    }

    @Override
    protected SubsetProbabilities getProbabilities(CountedSubsets countedSubsets) {
        double probabilities[] = new double[countedSubsets.counts.length];
        double numberOfDocuments = this.numberOfDocuments;
        for (int i = 0; i < probabilities.length; ++i) {
            if (countedSubsets.counts[i] >= minFrequency) {
                probabilities[i] = countedSubsets.counts[i] / numberOfDocuments;
            } else {
                probabilities[i] = 0;
            }
        }
        return new SubsetProbabilities(countedSubsets.segments, countedSubsets.conditions, probabilities);
    }

    @Override
    public String getName() {
        return "P_" + probModelName;
    }

    // @Override
    // public double getInverseProbability(int wordSetDef, int invertingWordSet, double[] probabilities) {
    // return probabilities[wordSetDef] - probabilities[wordSetDef | invertingWordSet];
    // }
}
