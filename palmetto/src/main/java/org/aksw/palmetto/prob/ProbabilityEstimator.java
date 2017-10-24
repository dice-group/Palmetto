/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.prob;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This interface defines the methods of a class that estimates the
 * probabilities of a given word set.
 * 
 * @author m.roeder
 * 
 */
public interface ProbabilityEstimator {

    /**
     * Estimates the probabilities for the given word sets and the given
     * segmentations.
     * 
     * @param wordsets
     *            word sets for which the probabilities should be estimated
     * @param definitions
     *            definitions how the word sets should be separated
     * @return the subset probabilities for the single word sets
     */
    public SubsetProbabilities[] getProbabilities(String wordsets[][], SegmentationDefinition definitions[]);

    /**
     * Returns the frequency determiner used by this estimator.
     * 
     * @return the frequency determiner used by this estimator
     */
    public FrequencyDeterminer getFrequencyDeterminer();

    /**
     * Sets the frequency determiner that should be used by this estimator.
     * 
     * @param determiner
     *            the frequency determiner used by this estimator
     */
    public void setFrequencyDeterminer(FrequencyDeterminer determiner);

    /**
     * Returns the name of this probability estimator.
     * 
     * @return the name of this probability estimator
     */
    public String getName();

    /**
     * Sets the minimum frequency a word (or word set) must have to get a
     * probability &gt; 0.
     * 
     * @param minFrequency
     *            the minimum frequency of a word
     */
    public void setMinFrequency(int minFrequency);
}
