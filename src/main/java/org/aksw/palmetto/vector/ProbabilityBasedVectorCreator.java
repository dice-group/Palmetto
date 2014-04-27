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

import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilitySupplier;

public class ProbabilityBasedVectorCreator extends AbstractVectorCreator {

    private static final String VECTOR_CREATOR_NAME = "V_p";

    public ProbabilityBasedVectorCreator(ProbabilitySupplier supplier) {
        super(supplier);
    }

    @Override
    public String getVectorCreatorName() {
        return VECTOR_CREATOR_NAME;
    }

    @Override
    protected SubsetVectors[] createVectors(String[][] wordsets, SubsetDefinition[] definitions,
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
