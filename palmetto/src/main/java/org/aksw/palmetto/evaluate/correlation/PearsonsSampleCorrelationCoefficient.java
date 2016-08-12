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
package org.aksw.palmetto.evaluate.correlation;

public class PearsonsSampleCorrelationCoefficient implements RankCorrelationCalculator {

    public double calculateRankCorrelation(final double x[], final double y[]) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The x and y array must have the same size!");
        }
        if (x.length < 2) {
            throw new IllegalArgumentException("The x and y array must have a minimum size of 2!");
        }

        double avgX = 0, avgY = 0;
        int pairs = 0;
        for (int i = 0; i < x.length; ++i) {
            if ((!Double.isNaN(x[i])) && (!Double.isNaN(y[i]))) {
                avgX += x[i];
                avgY += y[i];
                ++pairs;
            }
        }
        // If there are no valid pairs
        if (pairs == 0) {
            return 0;
        }
        avgX /= pairs;
        avgY /= pairs;

        double tempX, tempY, varianceX = 0, varianceY = 0, covarianceXY = 0;
        for (int i = 0; i < x.length; ++i) {
            if ((!Double.isNaN(x[i])) && (!Double.isNaN(y[i]))) {
                tempX = x[i] - avgX;
                varianceX += tempX * tempX;
                tempY = y[i] - avgY;
                varianceY += tempY * tempY;
                covarianceXY += tempX * tempY;
            }
        }
        if (varianceX == 0) {
            if (varianceY == 0) {
                return 1;
            } else {
                return 0;
            }
        }
        if (varianceY == 0) {
            return 0;
        }
        double corr = covarianceXY / (Math.sqrt(varianceX) * Math.sqrt(varianceY));
        // if(Double.isNaN(corr) || Double.isInfinite(corr)) {
        // System.out.println("STOP!");
        // }
        return corr;
    }
}
