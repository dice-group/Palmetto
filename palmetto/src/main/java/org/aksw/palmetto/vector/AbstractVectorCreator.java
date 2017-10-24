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
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilityEstimator;

/**
 * Abstract class containing the process of the vector creation.
 * 
 * @author m.roeder
 *
 */
public abstract class AbstractVectorCreator implements VectorCreator {

    private static final String VECTOR_SPACE_NAME = "V^Top";

    private ProbabilityEstimator supplier;

    public AbstractVectorCreator(ProbabilityEstimator supplier) {
        this.supplier = supplier;
    }

    @Override
    public SubsetVectors[] getVectors(String[][] wordsets, SegmentationDefinition[] definitions) {
        SubsetProbabilities probabilities[] = supplier.getProbabilities(wordsets, definitions);
        return createVectors(wordsets, definitions, probabilities);
    }

    protected abstract SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities);

    @Override
    public String getProbabilityEstimatorName() {
        return supplier.getName();
    }

    @Override
    public String getVectorSpaceName() {
        return VECTOR_SPACE_NAME;
    }

    @Override
    public void setMinFrequency(int minFrequency) {
        supplier.setMinFrequency(minFrequency);
    }

    public void setProbabilityEstimator(ProbabilityEstimator supplier) {
        this.supplier = supplier;
    }
}
