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
