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
 * Represents the metadata and the correlation results for a single coherence
 * measure (i.e. one line in the given data).
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class CoherenceValue implements Cloneable {
    protected double correlation;
    protected boolean containsNaN;
    protected String[] line;

    protected CoherenceValue() {
        super();
    }

    public CoherenceValue(double correlation, boolean containsNaN, String[] line) {
        super();
        update(correlation, containsNaN, line);
    }

    public CoherenceValue(CoherenceValue clone) {
        this(clone.correlation, clone.containsNaN, clone.line);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new CoherenceValue(this);
    }

    public void updateIfBetter(CoherenceValue newValues) {
        // If the given coherence has a better absolute correlation and the same NaN
        // state OR the given coherence has no NaN values while the current coherence
        // still has them
        if (((newValues.correlation > this.correlation) && (this.containsNaN == newValues.containsNaN))
                || ((this.containsNaN && !newValues.containsNaN))) {
            update(newValues);
        }
    }

    public void updateIfBetter(CoherenceValue newValues, boolean higherIsBetter) {
        // If the new coherence has no NaN while the current coherence has it OR
        // the NaN status is the same but the new value is better, i.e., depending on
        // the given flag, either the new value is higher or lower than the current
        // value.
        if ((this.containsNaN && !newValues.containsNaN) || ((this.containsNaN == newValues.containsNaN)
                && ((higherIsBetter && newValues.correlation > this.correlation)
                        || (!higherIsBetter && newValues.correlation < this.correlation)))) {
            update(newValues);
        }
    }

    protected void update(CoherenceValue newValues) {
        this.correlation = newValues.correlation;
        this.containsNaN = newValues.containsNaN;
        this.line = newValues.line;
    }

    protected void update(double correlation, boolean containsNaN, String[] line) {
        this.correlation = correlation;
        this.containsNaN = containsNaN;
        this.line = line;
    }

    public void update(String[] line, int startCorrValues, int endCorrValues, int avgCorrValues) {
        boolean containsNaN = false;
        int pos = startCorrValues;
        while (!containsNaN && (pos < endCorrValues)) {
            containsNaN = "NaN".equals(line[pos]);
            ++pos;
        }
        update(Double.parseDouble(line[avgCorrValues]), containsNaN, line);
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }

    /**
     * @return the containsNaN
     */
    public boolean isContainsNaN() {
        return containsNaN;
    }

    /**
     * @param containsNaN the containsNaN to set
     */
    public void setContainsNaN(boolean containsNaN) {
        this.containsNaN = containsNaN;
    }

    /**
     * @return the line
     */
    public String[] getLine() {
        return line;
    }

    /**
     * @param line the line to set
     */
    public void setLine(String[] line) {
        this.line = line;
    }

    public static CoherenceValue create(String[] line, int startCorrValues, int endCorrValues, int avgCorrValues) {
        CoherenceValue result = new CoherenceValue();
        result.update(line, startCorrValues, endCorrValues, avgCorrValues);
        return result;
    }
}