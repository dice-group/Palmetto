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

import org.aksw.palmetto.calculations.probbased.ProbabilityBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.OneOneAndSelf;

public class ProbCalcBasedVectorCreator extends AbstractVectorCreator {

    private ProbabilityBasedCalculation calculation;
    private OneOneAndSelf oneOneCreator = new OneOneAndSelf();

    public ProbCalcBasedVectorCreator(ProbabilitySupplier supplier, ProbabilityBasedCalculation calculation) {
        super(supplier);
        this.calculation = calculation;
    }

    @Override
    public String getVectorCreatorName() {
        return 'V' + calculation.getCalculationName().substring(1);
    }

    @Override
    protected SubsetVectors[] createVectors(String[][] wordsets, SubsetDefinition[] definitions,
            SubsetProbabilities[] probabilities) {
        SubsetVectors vectors[] = new SubsetVectors[wordsets.length];
        double currentVectors[][];
        SubsetDefinition oneOneDef = oneOneCreator.getSubsetDefinition(wordsets[0].length);
        SubsetProbabilities oneOneProbabilities = new SubsetProbabilities(oneOneDef.segments, oneOneDef.conditions,
                null);
        double calcResult[];
        int startId;
        for (int w = 0; w < wordsets.length; ++w) {
            oneOneProbabilities.probabilities = probabilities[w].probabilities;
            calcResult = calculation.calculateCoherenceValues(oneOneProbabilities);
            currentVectors = new double[wordsets[w].length][wordsets[w].length];
            startId = 0;
            for (int i = 0; i < wordsets[w].length; ++i) {
                System.arraycopy(calcResult, startId, currentVectors[i], 0, wordsets[w].length);
                startId += wordsets[w].length;
            }
            vectors[w] = new SubsetVectors(definitions[w].segments, definitions[w].conditions, currentVectors,
                    probabilities[w].probabilities);
        }
        return vectors;
    }

}
