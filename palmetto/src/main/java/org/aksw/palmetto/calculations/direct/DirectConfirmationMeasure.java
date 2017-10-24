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

import org.aksw.palmetto.calculations.ConfirmationMeasure;
import org.aksw.palmetto.data.SubsetProbabilities;

/**
 * This interface is implemented by all confirmation measures which are using
 * the probabilities directly.
 * 
 * @author Michael Röder
 * 
 */
public interface DirectConfirmationMeasure extends ConfirmationMeasure {

    /**
     * Calculates the confirmation values for the given subset probabilities.
     * 
     * @param subsetProbabilities
     *            subset probabilities used for the calculation
     * @return confirmation values
     */
    public abstract double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities);
}
