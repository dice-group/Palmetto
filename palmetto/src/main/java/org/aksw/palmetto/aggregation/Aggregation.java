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
package org.aksw.palmetto.aggregation;

/**
 * Aggregates the given confirmation values and returns a single coherence
 * value.
 * 
 * @author m.roeder
 * 
 */
public interface Aggregation {

    /**
     * The return value if the aggregation of the given values is not defined.
     */
    public static final double RETURN_VALUE_FOR_UNDEFINED = 0;

    /**
     * Aggregates the given confirmation values and returns a single coherence
     * value.
     * 
     * @param values
     *            values that should be aggregated
     * @return aggregated value
     */
    public double summarize(double values[]);

    /**
     * Aggregates the product of the given confirmation values and the given
     * weights and returns a single coherence value.
     * 
     * @param values
     *            values that should be aggregated
     * @param weights
     *            weights of the single values
     * @return aggregated value
     */
    public double summarize(double values[], double weights[]);

    /**
     * Returns the name of the aggregation.
     * 
     * @return name of the aggregation
     */
    public String getName();
}
