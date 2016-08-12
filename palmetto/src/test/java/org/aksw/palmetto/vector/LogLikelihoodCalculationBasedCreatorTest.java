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
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.aksw.palmetto.calculations.direct.LogLikelihoodConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class LogLikelihoodCalculationBasedCreatorTest extends AbstractProbCalcBasedVectorCreatorTest {

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
                                new double[][][] { {
                                        { 0, 0, 0 },
                                        { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) },
                                        { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },

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
                                new double[][][] { {
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5), Math.log(0.5) },
                                        { Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5) },
                                        { Math.log(0.5), Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON) } } },
                                "V_ll(1)", 1 },
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
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(0.5 / LogBasedCalculation.EPSILON),
                                                Math.log(0.5 / LogBasedCalculation.EPSILON) },
                                        { Math.log(3.0), Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                        { Math.log(3.0), 0, Math.log(1.0 / LogBasedCalculation.EPSILON) } } },
                                "V_ll(1)", 1 },
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
                                        { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                Math.log(LogBasedCalculation.EPSILON / 0.5),
                                                Math.log(LogBasedCalculation.EPSILON / 0.5) },
                                        { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0),
                                                Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                        { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 0,
                                                Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },
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
                                                { 0, 0, 0 },
                                                { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) },
                                                { 0, Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON), Math.log(0.5),
                                                        Math.log(0.5) },
                                                { Math.log(0.5), Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5) },
                                                { Math.log(0.5), Math.log(0.5),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5 / LogBasedCalculation.EPSILON),
                                                        Math.log(0.5 / LogBasedCalculation.EPSILON) },
                                                { Math.log(3.0), Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                                { Math.log(3.0), 0, Math.log(1.0 / LogBasedCalculation.EPSILON) } },
                                        {
                                                { Math.log(1.0 / LogBasedCalculation.EPSILON),
                                                        Math.log(LogBasedCalculation.EPSILON / 0.5),
                                                        Math.log(LogBasedCalculation.EPSILON / 0.5) },
                                                { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0),
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON), 0 },
                                                { Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 0,
                                                        Math.log(1.0 / LogBasedCalculation.EPSILON) } } }, "V_ll(1)", 1 },
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
                                        { 0, 0, 0 },
                                        { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) },
                                        { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },

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
                                new double[][][] { {
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2) },
                                        { Math.pow(Math.log(0.5), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5), 2) },
                                        { Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
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
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2) },
                                        { Math.pow(Math.log(3.0), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                        { Math.pow(Math.log(3.0), 2), 0,
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
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
                                        { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2),
                                                Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2) },
                                        { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2),
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                        { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2), 0,
                                                Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 },
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
                                                { 0, 0, 0 },
                                                { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) },
                                                { 0, Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2) },
                                                { Math.pow(Math.log(0.5), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5), 2) },
                                                { Math.pow(Math.log(0.5), 2), Math.pow(Math.log(0.5), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(0.5 / LogBasedCalculation.EPSILON), 2) },
                                                { Math.pow(Math.log(3.0), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                                { Math.pow(Math.log(3.0), 2), 0,
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } },
                                        {
                                                { Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2),
                                                        Math.pow(Math.log(LogBasedCalculation.EPSILON / 0.5), 2) },
                                                { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2),
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2), 0 },
                                                { Math.pow(Math.log(LogBasedCalculation.EPSILON * 3.0 / 2.0), 2), 0,
                                                        Math.pow(Math.log(1.0 / LogBasedCalculation.EPSILON), 2) } } },
                                "V_ll(2)", 2 } });
    }

    public LogLikelihoodCalculationBasedCreatorTest(int wordsetSize, double[][] probabilities,
            double[][][] expectedVectors, String expectedCreatorName, double gamma) {
        super(new LogLikelihoodConfirmationMeasure(), wordsetSize, probabilities, expectedVectors,
                expectedCreatorName, gamma);
    }
}
