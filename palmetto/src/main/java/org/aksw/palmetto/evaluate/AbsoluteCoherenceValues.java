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
public class AbsoluteCoherenceValues extends CoherenceValue implements Cloneable {
    protected double absoluteCorrelation;

    protected AbsoluteCoherenceValues() {
        super();
    }

    public AbsoluteCoherenceValues(double correlation, boolean containsNaN, String[] line) {
        super();
        update(correlation, containsNaN, line);
    }

    public AbsoluteCoherenceValues(AbsoluteCoherenceValues clone) {
        this(clone.correlation, clone.containsNaN, clone.line);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new AbsoluteCoherenceValues(this);
    }

    public void updateIfBetter(AbsoluteCoherenceValues newValues) {
        // If the given coherence has a better absolute correlation and the same NaN
        // state OR the given coherence has no NaN values while the current coherence
        // still has them
        if (((newValues.absoluteCorrelation > this.absoluteCorrelation)
                && (this.containsNaN == newValues.containsNaN)) || ((this.containsNaN && !newValues.containsNaN))) {
            update(newValues.absoluteCorrelation, newValues.containsNaN, newValues.line);
        }
    }

    protected void update(double correlation, boolean containsNaN, String[] line) {
        super.update(correlation, containsNaN, line);
        this.absoluteCorrelation = Math.abs(correlation);
    }

    /**
     * @return the absoluteCorrelation
     */
    public double getAbsoluteCorrelation() {
        return absoluteCorrelation;
    }

    /**
     * @param absoluteCorrelation the absoluteCorrelation to set
     */
    public void setAbsoluteCorrelation(double absoluteCorrelation) {
        this.absoluteCorrelation = absoluteCorrelation;
    }

    public static AbsoluteCoherenceValues create(String[] line, int startCorrValues, int endCorrValues, int avgCorrValues) {
        AbsoluteCoherenceValues result = new AbsoluteCoherenceValues();
        result.update(line, startCorrValues, endCorrValues, avgCorrValues);
        return result;
    }
}