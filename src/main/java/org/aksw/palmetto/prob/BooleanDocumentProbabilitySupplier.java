/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.prob;

import org.aksw.palmetto.corpus.BooleanDocumentSupportingAdapter;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetProbabilities;

public class BooleanDocumentProbabilitySupplier extends AbstractProbabilitySupplier {

    public static BooleanDocumentProbabilitySupplier create(CorpusAdapter adapter) {
        BooleanDocumentFrequencyDeterminer determiner = createFrequencyDeterminer(adapter);
        if (determiner != null) {
            return new BooleanDocumentProbabilitySupplier(determiner);
        }
        return null;
    }

    protected static BooleanDocumentFrequencyDeterminer createFrequencyDeterminer(CorpusAdapter adapter) {
        if (adapter instanceof BooleanDocumentSupportingAdapter) {
            return new BooleanDocumentFrequencyDeterminer((BooleanDocumentSupportingAdapter) adapter);
        }
        return null;
    }

    private int numberOfDocuments;

    protected BooleanDocumentProbabilitySupplier(BooleanDocumentFrequencyDeterminer freqDeterminer) {
        super(freqDeterminer);
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
    public String getProbabilityModelName() {
        return "P_bd";
    }
}
