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
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.LikelihoodConfirmationMeasure;
import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LikelihoodCalculationBasedCreatorTest extends AbstractProbCalcBasedVectorCreatorTest {

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays
                .asList(new Object[][] {
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 0 1 1
                         * 
                         * vector2 0 log(1/eps) log(1/eps)
                         * 
                         * vector3 0 log(1/eps) log(1/eps)
                         * 
                         * word1 has P(w_1)=1 which is a problem for likelihood
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { { { 0, 1, 1 },
                                        { 0, 1.0 / LogBasedCalculation.EPSILON, 1.0 / LogBasedCalculation.EPSILON },
                                        { 0, 1.0 / LogBasedCalculation.EPSILON, 1.0 / LogBasedCalculation.EPSILON } } },
                                "V_l(1)", 1 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(1/eps) log(0.5) log(0.5)
                         * 
                         * vector2 log(0.5) log(1/eps) log(0.5)
                         * 
                         * vector3 log(0.5) log(0.5) log(1/eps)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { 1.0 / LogBasedCalculation.EPSILON, 0.5, 0.5 },
                                        { 0.5, 1.0 / LogBasedCalculation.EPSILON, 0.5 },
                                        { 0.5, 0.5, 1.0 / LogBasedCalculation.EPSILON } } }, "V_l(1)", 1 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(2) log(2)
                         * 
                         * vector2 log(2) log(2) log(1)
                         * 
                         * vector3 log(2) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { 1.0 / LogBasedCalculation.EPSILON, 0.5 / LogBasedCalculation.EPSILON,
                                                0.5 / LogBasedCalculation.EPSILON },
                                        { 3.0, 1.0 / LogBasedCalculation.EPSILON, 1.0 },
                                        { 3.0, 1.0, 1.0 / LogBasedCalculation.EPSILON } } }, "V_l(1)", 1 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(8 * eps) log(8 * eps)
                         * 
                         * vector2 log(8 * eps) log(2) log(1)
                         * 
                         * vector3 log(8 * eps) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        { 1.0 / LogBasedCalculation.EPSILON, LogBasedCalculation.EPSILON / 0.5,
                                                LogBasedCalculation.EPSILON / 0.5 },
                                        { LogBasedCalculation.EPSILON * 3.0 / 2.0, 1.0 / LogBasedCalculation.EPSILON,
                                                1.0 },
                                        { LogBasedCalculation.EPSILON * 3.0 / 2.0, 1.0,
                                                1.0 / LogBasedCalculation.EPSILON } } }, "V_l(1)", 1 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        {
                                                { 0, 1, 1 },
                                                { 0, 1.0 / LogBasedCalculation.EPSILON,
                                                        1.0 / LogBasedCalculation.EPSILON },
                                                { 0, 1.0 / LogBasedCalculation.EPSILON,
                                                        1.0 / LogBasedCalculation.EPSILON } },
                                        { { 1.0 / LogBasedCalculation.EPSILON, 0.5, 0.5 },
                                                { 0.5, 1.0 / LogBasedCalculation.EPSILON, 0.5 },
                                                { 0.5, 0.5, 1.0 / LogBasedCalculation.EPSILON } },
                                        {
                                                { 1.0 / LogBasedCalculation.EPSILON, 0.5 / LogBasedCalculation.EPSILON,
                                                        0.5 / LogBasedCalculation.EPSILON },
                                                { 3.0, 1.0 / LogBasedCalculation.EPSILON, 1.0 },
                                                { 3.0, 1.0, 1.0 / LogBasedCalculation.EPSILON } },
                                        {
                                                { 1.0 / LogBasedCalculation.EPSILON, LogBasedCalculation.EPSILON / 0.5,
                                                        LogBasedCalculation.EPSILON / 0.5 },
                                                { LogBasedCalculation.EPSILON * 3.0 / 2.0,
                                                        1.0 / LogBasedCalculation.EPSILON, 1.0 },
                                                { LogBasedCalculation.EPSILON * 3.0 / 2.0, 1.0,
                                                        1.0 / LogBasedCalculation.EPSILON } } }, "V_l(1)", 1 },
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 log(1) log(1) log(1)
                         * 
                         * vector2 log(1) log(3/2) log(3/2)
                         * 
                         * vector3 log(1) log(3/2) log(3/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { {
                                        { 0, 1, 1 },
                                        { 0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) },
                                        { 0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } } }, "V_l(2)", 2 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(3/2) log(3/4) log(3/4)
                         * 
                         * vector2 log(3/4) log(3/2) log(3/4)
                         * 
                         * vector3 log(3/4) log(3/4) log(3/2)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { { { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 0.25, 0.25 },
                                        { 0.25, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 0.25 },
                                        { 0.25, 0.25, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } } }, "V_l(2)", 2 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(2) log(2)
                         * 
                         * vector2 log(2) log(2) log(1)
                         * 
                         * vector3 log(2) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                Math.pow(0.5 / LogBasedCalculation.EPSILON, 2),
                                                Math.pow(0.5 / LogBasedCalculation.EPSILON, 2) },
                                        { 9.0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 1.0 },
                                        { 9.0, 1.0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } } }, "V_l(2)", 2 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4) log(8 * eps) log(8 * eps)
                         * 
                         * vector2 log(8 * eps) log(2) log(1)
                         * 
                         * vector3 log(8 * eps) log(1) log(2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                Math.pow(LogBasedCalculation.EPSILON / 0.5, 2),
                                                Math.pow(LogBasedCalculation.EPSILON / 0.5, 2) },
                                        { Math.pow(LogBasedCalculation.EPSILON * 3.0 / 2.0, 2),
                                                Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 1.0 },
                                        { Math.pow(LogBasedCalculation.EPSILON * 3.0 / 2.0, 2), 1.0,
                                                Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } } }, "V_l(2)", 2 },
                        // all together
                        {
                                3,
                                new double[][] {
                                        { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0 },
                                        { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0, 0 },
                                        { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 },
                                        { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] {
                                        {
                                                { 0, 1, 1 },
                                                { 0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                        Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) },
                                                { 0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                        Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } },
                                        { { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 0.25, 0.25 },
                                                { 0.25, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 0.25 },
                                                { 0.25, 0.25, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } },
                                        {
                                                { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                        Math.pow(0.5 / LogBasedCalculation.EPSILON, 2),
                                                        Math.pow(0.5 / LogBasedCalculation.EPSILON, 2) },
                                                { 9.0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 1.0 },
                                                { 9.0, 1.0, Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } },
                                        {
                                                { Math.pow(1.0 / LogBasedCalculation.EPSILON, 2),
                                                        Math.pow(LogBasedCalculation.EPSILON / 0.5, 2),
                                                        Math.pow(LogBasedCalculation.EPSILON / 0.5, 2) },
                                                { Math.pow(LogBasedCalculation.EPSILON * 3.0 / 2.0, 2),
                                                        Math.pow(1.0 / LogBasedCalculation.EPSILON, 2), 1.0 },
                                                { Math.pow(LogBasedCalculation.EPSILON * 3.0 / 2.0, 2), 1.0,
                                                        Math.pow(1.0 / LogBasedCalculation.EPSILON, 2) } } }, "V_l(2)",
                                2 } });
    }

    public LikelihoodCalculationBasedCreatorTest(int wordsetSize, double[][] probabilities,
            double[][][] expectedVectors, String expectedCreatorName, double gamma) {
        super(new LikelihoodConfirmationMeasure(), wordsetSize, probabilities, expectedVectors, expectedCreatorName,
                gamma);
    }
}
