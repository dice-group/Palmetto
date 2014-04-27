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
package org.aksw.palmetto.calculations.probbased;


public abstract class AbstractUndefinedResultHandlingCoherenceCalculation implements ProbabilityBasedCalculation {

    private static final double DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS = Double.NaN;

    protected double resultIfCalcUndefined;

    public AbstractUndefinedResultHandlingCoherenceCalculation() {
        resultIfCalcUndefined = DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS;
    }

    public AbstractUndefinedResultHandlingCoherenceCalculation(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }

    public double getResultIfCalcUndefined() {
        return resultIfCalcUndefined;
    }

    public void setResultIfCalcUndefined(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }

    @Override
    public String getCalculationName() {
        if (Double.isNaN(resultIfCalcUndefined)) {
            return getName();
        } else {
            return getName() + "_(" + resultIfCalcUndefined + ")";
        }
    }

    protected abstract String getName();
}
