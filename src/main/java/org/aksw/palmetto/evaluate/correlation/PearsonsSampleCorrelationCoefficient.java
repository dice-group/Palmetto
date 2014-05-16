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
