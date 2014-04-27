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
import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.junit.Assert;
import org.junit.Test;

public abstract class AbstractVectorCreatorTest implements ProbabilitySupplier {

    private static final double DOUBLE_PRECISION_DELTA = 0.00000001;

    protected VectorCreator vectorCreator;
    protected SubsetCreator subsetCreator;
    protected int wordsetSize;
    protected double probabilities[];
    protected double expectedVectors[][];

    public AbstractVectorCreatorTest(VectorCreator vectorCreator, SubsetCreator subsetCreator, int wordsetSize,
            double[] probabilities, double expectedVectors[][]) {
        this.vectorCreator = vectorCreator;
        vectorCreator.setProbabilitySupplier(this);
        this.probabilities = probabilities;
        this.wordsetSize = wordsetSize;
        this.subsetCreator = subsetCreator;
        this.expectedVectors = expectedVectors;
    }

    @Test
    public void test() {
        SubsetVectors vectors = vectorCreator.getVectors(new String[1][wordsetSize],
                new SubsetDefinition[] { subsetCreator.getSubsetDefinition(wordsetSize) })[0];
        Assert.assertEquals(expectedVectors.length, vectors.vectors.length);
        for (int i = 0; i < expectedVectors.length; i++) {
            Assert.assertArrayEquals("The " + i + "th vectors are not equal.", expectedVectors[i], vectors.vectors[i],
                    DOUBLE_PRECISION_DELTA);
        }
    }

    @Override
    public SubsetProbabilities[] getProbabilities(String[][] wordsets, SubsetDefinition[] definitions) {
        return new SubsetProbabilities[] { new SubsetProbabilities(definitions[0].segments, definitions[0].conditions,
                probabilities) };
    }

    @Override
    @Deprecated
    public double getInverseProbability(int wordSetDef, int invertingWordSet, double[] probabilities) {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public FrequencyDeterminer getFrequencyDeterminer() {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public String getProbabilityModelName() {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }

    @Override
    public void setMinFrequency(int minFrequency) {
        throw new IllegalAccessError("This method shouldn't be accessed. Therefore it have not been implemented.");
    }
}
