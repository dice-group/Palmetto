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

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;

public abstract class AbstractProbabilitySupplier implements
        ProbabilitySupplier {

    public static final int DEFAULT_MIN_FREQUENCY = 10;

    protected FrequencyDeterminer freqDeterminer;
    protected int minFrequency = DEFAULT_MIN_FREQUENCY;

    protected AbstractProbabilitySupplier(FrequencyDeterminer freqDeterminer) {
        this.freqDeterminer = freqDeterminer;
    }

    public SubsetProbabilities[] getProbabilities(String wordsets[][],
            SubsetDefinition definitions[]) {
        CountedSubsets subsets[] = freqDeterminer.determineCounts(wordsets,
                definitions);
        SubsetProbabilities probabilities[] = new SubsetProbabilities[subsets.length];
        for (int i = 0; i < subsets.length; i++) {
            probabilities[i] = getProbabilities(subsets[i]);
        }
        return probabilities;
    }

    protected abstract SubsetProbabilities getProbabilities(
            CountedSubsets countedSubsets);

    public void setMinFrequency(int minFrequency) {
        this.minFrequency = minFrequency;
    }

    public int getMinFrequency() {
        return minFrequency;
    }

    public FrequencyDeterminer getFrequencyDeterminer() {
        return freqDeterminer;
    }

    public void setFrequencyDeterminer(FrequencyDeterminer freqDeterminer) {
        this.freqDeterminer = freqDeterminer;
    }
}
