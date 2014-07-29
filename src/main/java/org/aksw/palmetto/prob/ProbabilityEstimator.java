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

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This interface defines the methods of a class that estimates the probabilities of a given word set.
 * 
 * @author m.roeder
 * 
 */
public interface ProbabilityEstimator {

    /**
     * Estimates the probabilities for the given word sets and the given segmentations.
     * 
     * @param wordsets
     * @param definitions
     * @return
     */
    public SubsetProbabilities[] getProbabilities(String wordsets[][],
            SegmentationDefinition definitions[]);

    /**
     * Returns the frequency determiner used by this estimator.
     * 
     * @return
     */
    public FrequencyDeterminer getFrequencyDeterminer();

    /**
     * Sets the frequency determiner that should be used by this estimator.
     * 
     * @param determiner
     */
    public void setFrequencyDeterminer(FrequencyDeterminer determiner);

    /**
     * Returns the name of this probability estimator.
     * 
     * @return
     */
    public String getName();

    /**
     * Sets the minimum frequency a word (or word set) must have to get a probability > 0.
     * 
     * @param minFrequency
     */
    public void setMinFrequency(int minFrequency);
}
