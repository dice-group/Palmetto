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
 * This vector creator uses the marginal probabilities for the vector creation.
 * 
 * @author m.roeder
 * 
 */
public class ProbabilityBasedVectorCreator extends AbstractVectorCreator {

    private static final String VECTOR_CREATOR_NAME = "V_p";

    public ProbabilityBasedVectorCreator(ProbabilityEstimator supplier) {
        super(supplier);
    }

    @Override
    public String getVectorCreatorName() {
        return VECTOR_CREATOR_NAME;
    }

    @Override
    protected SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities) {
        SubsetVectors vectors[] = new SubsetVectors[wordsets.length];
        double currentVectors[][];
        int bit1;
        for (int w = 0; w < wordsets.length; ++w) {
            currentVectors = new double[wordsets[w].length][wordsets[w].length];
            for (int i = 0; i < wordsets[w].length; ++i) {
                bit1 = 1 << i;
                currentVectors[i][i] = probabilities[w].probabilities[bit1];
                for (int j = i + 1; j < wordsets[w].length; ++j) {
                    currentVectors[i][j] = probabilities[w].probabilities[bit1 | (1 << j)];
                    currentVectors[j][i] = currentVectors[i][j];
                }
            }
            vectors[w] = new SubsetVectors(definitions[w].segments, definitions[w].conditions, currentVectors,
                    probabilities[w].probabilities);
        }
        return vectors;
    }

}
