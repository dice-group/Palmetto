/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
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
     * @return
     */
    public abstract double[] calculateConfirmationValues(SubsetProbabilities subsetProbabilities);
}
