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
package org.aksw.palmetto.calculations.direct;

/**
 * An abstract class of all confirmation measures which have to handle undefined
 * results.
 * 
 * @author Michael Röder
 * 
 */
public abstract class AbstractUndefinedResultHandlingConfirmationMeasure implements DirectConfirmationMeasure {

    /**
     * Default value for {@link #resultIfCalcUndefined} = {@value} .
     */
    private static final double DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS = 0;

    /**
     * Value which is returned if the calculation is not defined.
     */
    protected double resultIfCalcUndefined;

    /**
     * Constructor.
     */
    public AbstractUndefinedResultHandlingConfirmationMeasure() {
        resultIfCalcUndefined = DEFAULT_RESULT_FOR_UNDIFEND_CALCULATIONS;
    }

    /**
     * Constructor.
     * 
     * @param resultIfCalcUndefined
     *            value which should be returned if the calculation is not
     *            defined.
     */
    public AbstractUndefinedResultHandlingConfirmationMeasure(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }

    public double getResultIfCalcUndefined() {
        return resultIfCalcUndefined;
    }

    public void setResultIfCalcUndefined(double resultIfCalcUndefined) {
        this.resultIfCalcUndefined = resultIfCalcUndefined;
    }
}
