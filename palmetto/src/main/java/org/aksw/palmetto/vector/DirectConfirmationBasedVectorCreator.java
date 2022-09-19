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

    private DirectConfirmationMeasure calculation;
    private OneOneAndSelf oneOneAndSelfCreator = new OneOneAndSelf();
    private double gamma;

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
    public SubsetVectors[] createVectors(String[][] wordsets, SegmentationDefinition[] definitions,
            SubsetProbabilities[] probabilities) {
        SubsetVectors vectors[] = new SubsetVectors[wordsets.length];
        SegmentationDefinition oneOneAndSelfDef = oneOneAndSelfCreator.getSubsetDefinition(wordsets[0].length);
        SubsetProbabilities oneOneAndSelfProbabilities = new SubsetProbabilities(oneOneAndSelfDef.segments,
                oneOneAndSelfDef.conditions, null);
        for (int w = 0; w < wordsets.length; ++w) {
            try {
                vectors[w] = createVectors(wordsets[w].length, definitions[w], oneOneAndSelfProbabilities,
                        probabilities[w]);
            } catch (Exception e) {
                System.err.println("ERROR w=" + w + " wordsets[w]=" + Arrays.toString(wordsets[w]));
            }
        }
        return vectors;
    }

    /**
     * Creates the vectors for a single wordset.
     * 
     * @param wordsetLength              the length of the wordset
     * @param definition                 the definition of the segmentations that
     *                                   will be copied into the created subset
     *                                   vector definition
     * @param oneOneAndSelfProbabilities this probabilities instance should contain
     *                                   the segmentation of the
     *                                   {@link OneOneAndSelf} class for the given
     *                                   word set. Note that although it is a
     *                                   probabilities instance probabilities
     *                                   instance, its probabilities will be
     *                                   internally overridden with the next
     *                                   probabilities parameter. Note this
     *                                   parameter is mainly used to reuse the
     *                                   generated OneOneAndSelf segmentation for a
     *                                   given set of wordsets.
     * @param probabilities              the probabilities for the given word set.
     * @return subset vectors that comprise the vectors for the given wordset length
     *         based on the given probabilities and the direct confirmation measure
     *         of this class ({@link #calculation}). The subset vector instance will
     *         also contain the given segmentation information as well as the given
     *         probabilities.
     */
    public SubsetVectors createVectors(int wordsetLength, SegmentationDefinition definition,
            SubsetProbabilities oneOneAndSelfProbabilities, SubsetProbabilities probabilities) {
        double currentVectors[][];
        double calcResult[];
        int startId;
        oneOneAndSelfProbabilities.probabilities = probabilities.probabilities;
        calcResult = calculation.calculateConfirmationValues(oneOneAndSelfProbabilities);
        currentVectors = new double[wordsetLength][wordsetLength];
        startId = 0;
        for (int i = 0; i < wordsetLength; ++i) {
            System.arraycopy(calcResult, startId, currentVectors[i], 0, wordsetLength);
            startId += wordsetLength;
        }
        if (gamma != 1) {
            for (int i = 0; i < wordsetLength; ++i) {
                for (int j = 0; j < wordsetLength; ++j) {
                    currentVectors[i][j] = Math.pow(currentVectors[i][j], gamma);
                }
            }
        }
        return new SubsetVectors(definition.segments, definition.conditions, currentVectors,
                probabilities.probabilities);
    }

    public void setGamma(double gamma) {
        this.gamma = gamma;
    }

    public double getGamma() {
        return gamma;
    }
}
