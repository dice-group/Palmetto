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
