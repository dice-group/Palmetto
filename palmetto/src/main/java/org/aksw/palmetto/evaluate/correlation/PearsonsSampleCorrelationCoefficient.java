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
package org.aksw.palmetto.evaluate.correlation;

/**
 * Class implementing Pearsons sample correlation coefficient for two given,
 * paired samples.
 * 
 * @author Michael R&ouml;der (michael.roeder@uni-paderborn.de)
 *
 */
public class PearsonsSampleCorrelationCoefficient implements RankCorrelationCalculator {

    /**
     * Calculates the Pearson sample correlation coefficient for the two given,
     * paired samples.
     * 
     * @param x
     *            the x samples
     * @param y
     *            the y samples
     * @return Parsons sample correlation coefficient
     * @throws IllegalArgumentException
     *             if x and y have not the same size or have less than 2 elements.
     */
    public double calculateRankCorrelation(final double x[], final double y[]) {
        if (x.length != y.length) {
            throw new IllegalArgumentException("The x and y array must have the same size!");
        }
        if (x.length < 2) {
            throw new IllegalArgumentException("The x and y array must have a minimum size of 2!");
        }

        int pairs = countValidPairs(x, y);
        // If there are no valid pairs
        if (pairs == 0) {
            return 0;
        }

        double averages[] = calculatePairedAverage(x, y);

        double variances[] = calculateCoVariances(x, averages[0], y, averages[1]);
        if (variances[0] == 0) {
            if (variances[1] == 0) {
                return 1;
            } else {
                return 0;
            }
        }
        if (variances[1] == 0) {
            return 0;
        }
        // Covar. / (sqrt(varianceX) + sqrt(varianceY))
        double corr = variances[2] / (Math.sqrt(variances[0]) * Math.sqrt(variances[1]));
        return corr;
    }

    /**
     * Counts the number of valid pairs (x,y) , i.e., pairs where {@code x != NaN}
     * and {@code y != NaN}.
     * 
     * @param x
     *            the x samples
     * @param y
     *            the y samples
     * @return the number of valid pairs
     */
    protected int countValidPairs(final double x[], final double y[]) {
        int pairs = 0;
        for (int i = 0; i < x.length; ++i) {
            if ((!Double.isNaN(x[i])) && (!Double.isNaN(y[i]))) {
                ++pairs;
            }
        }
        return pairs;
    }

    /**
     * Calculates the average values of the given (x,y) pairs for all pairs that
     * have {@code x != NaN} and {@code y != NaN}. The returned array contains the
     * average x as first and the average y as second element.
     * 
     * @param x
     *            the x samples
     * @param y
     *            the y samples
     * @return the averages of x and y.
     */
    protected double[] calculatePairedAverage(final double x[], final double y[]) {
        double[] averages = new double[2];
        int pairs = 0;
        for (int i = 0; i < x.length; ++i) {
            if ((!Double.isNaN(x[i])) && (!Double.isNaN(y[i]))) {
                averages[0] += x[i];
                averages[1] += y[i];
                ++pairs;
            }
        }
        averages[0] /= pairs;
        averages[1] /= pairs;
        return averages;
    }

    /**
     * Calculates the variance and covariance for all valid (x,y) pairs. The
     * returned array contains the variance of x as first, the variance of y as
     * second and their covariance as third element.
     * 
     * @param x
     *            the x samples
     * @param y
     *            the y samples
     * @return the variances and covariance of x and y.
     */
    protected double[] calculateCoVariances(final double x[], double avgX, final double y[], double avgY) {
        double tempX;
        double tempY;
        double variances[] = new double[3];
        for (int i = 0; i < x.length; ++i) {
            if ((!Double.isNaN(x[i])) && (!Double.isNaN(y[i]))) {
                tempX = x[i] - avgX;
                variances[0] += tempX * tempX;
                tempY = y[i] - avgY;
                variances[1] += tempY * tempY;
                variances[2] += tempX * tempY;
            }
        }
        return variances;
    }
}
