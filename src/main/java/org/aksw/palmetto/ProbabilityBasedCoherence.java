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
package org.aksw.palmetto;

import org.aksw.palmetto.calculations.probbased.ProbabilityBasedCalculation;
import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.data.SubsetProbabilities;
import org.aksw.palmetto.prob.ProbabilitySupplier;
import org.aksw.palmetto.subsets.SubsetCreator;
import org.aksw.palmetto.sum.Summarization;
import org.aksw.palmetto.weight.Weighter;

public class ProbabilityBasedCoherence implements Coherence {

    protected SubsetCreator subsetCreator;
    protected ProbabilitySupplier probSupplier;
    protected ProbabilityBasedCalculation calculation;
    protected Summarization summarizer;
    protected Weighter weighter;
    protected String dataSource = "unkown";

    public ProbabilityBasedCoherence(SubsetCreator subsetCreator, ProbabilitySupplier probSupplier, ProbabilityBasedCalculation calculation,
            Summarization summarizer, Weighter weighter) {
        this.subsetCreator = subsetCreator;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.weighter = weighter;
    }

    public ProbabilityBasedCoherence(SubsetCreator subsetCreator, ProbabilitySupplier probSupplier, ProbabilityBasedCalculation calculation,
            Summarization summarizer, Weighter weighter, String dataSource) {
        this.subsetCreator = subsetCreator;
        this.probSupplier = probSupplier;
        this.calculation = calculation;
        this.summarizer = summarizer;
        this.weighter = weighter;
        this.dataSource = dataSource;
    }

    @Override
    public double[] calculateCoherences(String[][] wordsets) {
        // create subset definitions
        SubsetDefinition definitions[] = new SubsetDefinition[wordsets.length];
        for (int i = 0; i < definitions.length; i++) {
            definitions[i] = subsetCreator.getSubsetDefinition(wordsets[i].length);
        }

        // get the probabilities
        SubsetProbabilities probabilities[] = probSupplier.getProbabilities(wordsets, definitions);
        definitions = null;

        double coherences[] = new double[probabilities.length];
        for (int i = 0; i < probabilities.length; i++) {
            coherences[i] = summarizer.summarize(calculation.calculateCoherenceValues(probabilities[i]),
                    weighter.createWeights(probabilities[i]));
        }
        return coherences;
    }

    @Override
    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append("C(D_");
        builder.append(dataSource);
        builder.append(',');
        builder.append(probSupplier.getProbabilityModelName());
        builder.append(',');
        builder.append(subsetCreator.getName());
        builder.append(',');
        builder.append(calculation.getCalculationName());
        builder.append(',');
        builder.append(summarizer.getName());
        builder.append(',');
        builder.append(weighter.getName());
        builder.append(')');
        return builder.toString();
    }
}
