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
package org.aksw.palmetto.vector;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilityEstimator;

/**
 * Interface for the vector creation.
 * 
 * @author m.roeder
 * 
 */
public interface VectorCreator {

    /**
     * Creates vectors for the given word sets and their segmentations.
     * 
     * @param wordsets
     * @param definitions
     * @return
     */
    public SubsetVectors[] getVectors(String wordsets[][], SegmentationDefinition definitions[]);

    /**
     * Sets the probability estimator used by the vector creator.
     * 
     * @param supplier
     */
    public void setProbabilityEstimator(ProbabilityEstimator supplier);

    /**
     * Calls {@link ProbabilityEstimator#getName()} of the probability estimator and returns the
     * name of
     * the estimator.
     * 
     * @return
     */
    public String getProbabilityEstimatorName();

    /**
     * Returns the name of the vector space.
     * 
     * @return
     */
    public String getVectorSpaceName();

    /**
     * Returns the name of the direct confirmation measure which is used to create the vectors.
     * 
     * @return
     */
    public String getVectorCreatorName();

    /**
     * Sets the minimum frequency of the probability estimator.
     * 
     * @param minFrequency
     */
    public void setMinFrequency(int minFrequency);
}
