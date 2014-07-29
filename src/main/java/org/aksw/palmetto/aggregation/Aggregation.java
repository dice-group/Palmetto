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
package org.aksw.palmetto.aggregation;

/**
 * Aggregates the given confirmation values and returns a single coherence value.
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
     * Aggregates the given confirmation values and returns a single coherence value.
     * 
     * @param values
     * @return
     */
    public double summarize(double values[]);

    /**
     * Aggregates the product of the given confirmation values and the given weights and returns a single coherence
     * value.
     * 
     * @param values
     * @param weights
     * @return
     */
    public double summarize(double values[], double weights[]);

    /**
     * Returns the name of the aggregation.
     * 
     * @return
     */
    public String getName();
}
