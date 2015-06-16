/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
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
