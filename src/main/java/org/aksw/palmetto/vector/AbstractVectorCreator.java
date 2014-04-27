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

public abstract class AbstractVectorCreator implements VectorCreator {

    private static final String VECTOR_SPACE_NAME = "V^Top";

    private ProbabilitySupplier supplier;

    public AbstractVectorCreator(ProbabilitySupplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public SubsetVectors[] getVectors(String[][] wordsets, SubsetDefinition[] definitions) {
        SubsetProbabilities probabilities[] = supplier.getProbabilities(wordsets, definitions);
        return createVectors(wordsets, definitions, probabilities);
    }

    protected abstract SubsetVectors[] createVectors(String[][] wordsets, SubsetDefinition[] definitions,
            SubsetProbabilities[] probabilities);

    @Override
    public String getProbabilityModelName() {
        return supplier.getProbabilityModelName();
    }

    @Override
    public String getVectorSpaceName() {
        return VECTOR_SPACE_NAME;
    }

    @Override
    public void setMinFrequency(int minFrequency) {
        supplier.setMinFrequency(minFrequency);
    }

    public void setProbabilitySupplier(ProbabilitySupplier supplier) {
        this.supplier = supplier;
    }
}
