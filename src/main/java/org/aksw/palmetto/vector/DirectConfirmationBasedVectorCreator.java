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
package org.aksw.palmetto.vector;

import java.util.Arrays;

import org.aksw.palmetto.calculations.direct.DirectConfirmationMeasure;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.data.SubsetVectors;
import org.aksw.palmetto.prob.ProbabilityEstimator;
import org.aksw.palmetto.subsets.OneOneAndSelf;

/**
 * This vector creator uses a direct confirmation measure to create the vectors.
 * 
 * @author m.roeder
 * 
 */
public class DirectConfirmationBasedVectorCreator extends AbstractVectorCreator {

    private static final int DEFAULT_GAMMA = 2;

    private DirectConfirmationMeasure calculation;
    private OneOneAndSelf oneOneAndSelfCreator = new OneOneAndSelf();
    private double gamma;

    public DirectConfirmationBasedVectorCreator(ProbabilityEstimator supplier, DirectConfirmationMeasure calculation) {
        super(supplier);
        this.calculation = calculation;
        this.gamma = DEFAULT_GAMMA;
    }

    public DirectConfirmationBasedVectorCreator(ProbabilityEstimator supplier, DirectConfirmationMeasure calculation,
            double gamma) {
        super(supplier);
        this.calculation = calculation;
        this.gamma = gamma;
    }

    @Override
    public String getVectorCreatorName() {
        return 'V' + calculation.getName().substring(1) + "(" + ((int) gamma) + ")";
    }

    @Override
    protected SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities) {
        SubsetVectors vectors[] = new SubsetVectors[wordsets.length];
        double currentVectors[][];
        SegmentationDefinition oneOneAndSelfDef = oneOneAndSelfCreator.getSubsetDefinition(wordsets[0].length);
        SubsetProbabilities oneOneAndSelfProbabilities = new SubsetProbabilities(oneOneAndSelfDef.segments,
                oneOneAndSelfDef.conditions, null);
        double calcResult[];
        int startId;
        for (int w = 0; w < wordsets.length; ++w) {
            oneOneAndSelfProbabilities.probabilities = probabilities[w].probabilities;
            calcResult = calculation.calculateConfirmationValues(oneOneAndSelfProbabilities);
            currentVectors = new double[wordsets[w].length][wordsets[w].length];
            startId = 0;
            try {
                for (int i = 0; i < wordsets[w].length; ++i) {
                    System.arraycopy(calcResult, startId, currentVectors[i], 0, wordsets[w].length);
                    startId += wordsets[w].length;
                }
            } catch (Exception e) {
                System.err.println("ERROR w=" + w + " wordsets[w]=" + Arrays.toString(wordsets[w]) + " calcResult="
                        + Arrays.toString(calcResult));
            }

            if (gamma != 1) {
                for (int i = 0; i < wordsets[w].length; ++i) {
                    for (int j = 0; j < currentVectors[i].length; ++j) {
                        currentVectors[i][j] = Math.pow(currentVectors[i][j], gamma);
                    }
                }
            }
            vectors[w] = new SubsetVectors(definitions[w].segments, definitions[w].conditions, currentVectors,
                    probabilities[w].probabilities);
        }
        return vectors;
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGamma() {
        return gamma;
    }
}
