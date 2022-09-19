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
package org.aksw.palmetto.evaluate;

/**
 * This simple structure represents an array of coherence values.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class CoherenceValueArray extends CoherenceValue {

    protected double[] values;
    protected double stdDev;

    public CoherenceValueArray() {
        super();
    }

    public CoherenceValueArray(double correlation, double values[], boolean containsNaN, String[] line) {
        super(correlation, containsNaN, line);
        this.values = values;
    }

    public CoherenceValueArray(double correlation, double stdDev, double values[], boolean containsNaN, String[] line) {
        super(correlation, containsNaN, line);
        this.stdDev = stdDev;
        this.values = values;
    }

    public void update(String[] line, int startCorrValues, int endCorrValues, int avgCorrValues) {
        boolean containsNaN = false;
        values = new double[endCorrValues - startCorrValues];
        for (int i = 0; i < values.length; ++i) {
            values[i] = Double.parseDouble(line[i + startCorrValues]);
            containsNaN = containsNaN || Double.isNaN(values[i]);
        }
        update(Double.parseDouble(line[avgCorrValues]), containsNaN, line);
    }

    protected void update(CoherenceValue newValues) {
        super.update(newValues);
        if(newValues instanceof CoherenceValueArray) {
            this.values = ((CoherenceValueArray) newValues).values;
            this.stdDev = ((CoherenceValueArray) newValues).stdDev;
        }
    }

    /**
     * @return the values
     */
    public double[] getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(double[] values) {
        this.values = values;
    }

    /**
     * @return the stdDev
     */
    public double getStdDev() {
        return stdDev;
    }

    /**
     * @param stdDev the stdDev to set
     */
    public void setStdDev(double stdDev) {
        this.stdDev = stdDev;
    }

    /**
     * Calculates the arithmetic mean and the standard deviation of the values
     * array.
     */
    public void summarize() {
        double sum = 0;
        for (int i = 0; i < values.length; ++i) {
            sum += values[i];
        }
        double mean = sum / values.length;
        setCorrelation(mean);
        sum = 0;
        double temp;
        for (int i = 0; i < values.length; ++i) {
            temp = mean - values[i];
            sum += temp * temp;
        }
        sum = sum / values.length;
        stdDev = Math.sqrt(sum);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CoherenceValueArray(correlation, stdDev, values, containsNaN, line);
    }

    public static CoherenceValueArray create(String[] line, int startCorrValues, int endCorrValues, int avgCorrValues) {
        CoherenceValueArray result = new CoherenceValueArray();
        result.update(line, startCorrValues, endCorrValues, avgCorrValues);
        return result;
    }
}
