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
