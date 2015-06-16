/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.vector;

import java.util.Arrays;
import java.util.Collection;

import org.aksw.palmetto.calculations.direct.LogBasedCalculation;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class NormalizedLogRatioCalculationBasedCreatorTest extends AbstractProbCalcBasedVectorCreatorTest {

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
                         * vector1 log(1+eps)/-log(1+eps) 0 0
                         * 
                         * vector2 0 log(3/2)/-log(2/3) log(3/2)/-log(2/3)
                         * 
                         * vector3 0 log(3/2)/-log(2/3) log(3/2)/-log(2/3)
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { {
                                        { -1, 0, 0 },
                                        { 0, Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)),
                                                Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)) },
                                        { 0, Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)),
                                                Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)) } } }, "V_nlr(1)", 1 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(3/2)/-log(2/3) log(3/4)/-log(1/3)
                         * log(3/4)/-log(1/3)
                         * 
                         * vector2 log(3/4)/-log(1/3) log(3/2)/-log(2/3)
                         * log(3/4)/-log(1/3)
                         * 
                         * vector3 log(3/4)/-log(1/3) log(3/4)/-log(1/3)
                         * log(3/2)/-log(2/3)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { {
                                        { Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0),
                                                Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0) },
                                        { Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0),
                                                Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0) },
                                        { Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0) } } }, "V_nlr(1)", 1 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4)/-log(1/4) log(2)/-log(1/4)
                         * log(2)/-log(1/4)
                         * 
                         * vector2 log(2)/-log(1/4) log(2)/-log(1/2) 0
                         * 
                         * vector3 log(2)/-log(1/4) 0 log(2)/-log(1/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { Math.log(4.0) / (-Math.log(1.0 / 4.0)),
                                                Math.log(2.0) / (-Math.log(1.0 / 4.0)),
                                                Math.log(2.0) / (-Math.log(1.0 / 4.0)) },
                                        { Math.log(2.0) / (-Math.log(1.0 / 4.0)),
                                                Math.log(2.0) / (-Math.log(1.0 / 2.0)), 0 },
                                        { Math.log(2.0) / (-Math.log(1.0 / 4.0)), 0,
                                                Math.log(2.0) / (-Math.log(1.0 / 2.0)) } } }, "V_nlr(1)", 1 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4)/-log(1/4) log(8 * eps)/-log(eps) log(8
                         * * eps)/-log(eps)
                         * 
                         * vector2 log(8 * eps)/-log(eps) log(2)/-log(1/2) 0
                         * 
                         * vector3 log(8 * eps)/-log(eps) 0 log(2)/-log(1/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        {
                                                Math.log(4.0) / (-Math.log(1.0 / 4.0)),
                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                        / -Math.log(LogBasedCalculation.EPSILON),
                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                        / -Math.log(LogBasedCalculation.EPSILON) },
                                        {
                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                        / -Math.log(LogBasedCalculation.EPSILON),
                                                Math.log(2.0) / (-Math.log(1.0 / 2.0)), 0 },
                                        {
                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                        / -Math.log(LogBasedCalculation.EPSILON), 0,
                                                Math.log(2.0) / (-Math.log(1.0 / 2.0)) } } }, "V_nlr(1)", 1 },
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
                                                { -1, 0, 0 },
                                                { 0, Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)),
                                                        Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)) },
                                                { 0, Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)),
                                                        Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)) } },
                                        {
                                                { Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0),
                                                        Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                        Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0) },
                                                { Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                        Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0),
                                                        Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0) },
                                                { Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                        Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0),
                                                        Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0) } },
                                        {
                                                { Math.log(4.0) / (-Math.log(1.0 / 4.0)),
                                                        Math.log(2.0) / (-Math.log(1.0 / 4.0)),
                                                        Math.log(2.0) / (-Math.log(1.0 / 4.0)) },
                                                { Math.log(2.0) / (-Math.log(1.0 / 4.0)),
                                                        Math.log(2.0) / (-Math.log(1.0 / 2.0)), 0 },
                                                { Math.log(2.0) / (-Math.log(1.0 / 4.0)), 0,
                                                        Math.log(2.0) / (-Math.log(1.0 / 2.0)) } },
                                        {
                                                {
                                                        Math.log(4.0) / (-Math.log(1.0 / 4.0)),
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON),
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON) },
                                                {
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON),
                                                        Math.log(2.0) / (-Math.log(1.0 / 2.0)), 0 },
                                                {
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON), 0,
                                                        Math.log(2.0) / (-Math.log(1.0 / 2.0)) } } }, "V_nlr(1)", 1 },
                        /*
                         * word1 1 1 1
                         * 
                         * word2 0 1 1
                         * 
                         * word3 0 1 1
                         * 
                         * vector1 log(1+eps)/-log(1+eps) 0 0
                         * 
                         * vector2 0 log(3/2)/-log(2/3) log(3/2)/-log(2/3)
                         * 
                         * vector3 0 log(3/2)/-log(2/3) log(3/2)/-log(2/3)
                         */
                        {
                                3,
                                new double[][] { { 0, 1.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0, 2.0 / 3.0,
                                        2.0 / 3.0 } },
                                new double[][][] { {
                                        { 1, 0, 0 },
                                        { 0, Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2),
                                                Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2) },
                                        { 0, Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2),
                                                Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2) } } },
                                "V_nlr(2)", 2 },

                        /*
                         * word1 0 1 1
                         * 
                         * word2 1 0 1
                         * 
                         * word3 1 1 0
                         * 
                         * vector1 log(3/2)/-log(2/3) log(3/4)/-log(1/3)
                         * log(3/4)/-log(1/3)
                         * 
                         * vector2 log(3/4)/-log(1/3) log(3/2)/-log(2/3)
                         * log(3/4)/-log(1/3)
                         * 
                         * vector3 log(3/4)/-log(1/3) log(3/4)/-log(1/3)
                         * log(3/2)/-log(2/3)
                         */{
                                3,
                                new double[][] { { 0, 2.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 2.0 / 3.0, 1.0 / 3.0, 1.0 / 3.0,
                                        0 } },
                                new double[][][] { {
                                        { Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2) },
                                        { Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2) },
                                        { Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2) } } },
                                "V_nlr(2)", 2 },
                        /*
                         * word1 0 0 0 1
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4)/-log(1/4) log(2)/-log(1/4)
                         * log(2)/-log(1/4)
                         * 
                         * vector2 log(2)/-log(1/4) log(2)/-log(1/2) 0
                         * 
                         * vector3 log(2)/-log(1/4) 0 log(2)/-log(1/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0.25, 0.5, 0.25, 0.25, 0.25 } },
                                new double[][][] { {
                                        { Math.pow(Math.log(4.0) / (-Math.log(1.0 / 4.0)), 2),
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2),
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2) },
                                        { Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2),
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2), 0 },
                                        { Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2), 0,
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2) } } }, "V_nlr(2)", 2 },
                        /*
                         * word1 1 0 0 0
                         * 
                         * word2 0 1 0 1
                         * 
                         * word3 0 0 1 1
                         * 
                         * vector1 log(4)/-log(1/4) log(8 * eps)/-log(eps) log(8
                         * * eps)/-log(eps)
                         * 
                         * vector2 log(8 * eps)/-log(eps) log(2)/-log(1/2) 0
                         * 
                         * vector3 log(8 * eps)/-log(eps) 0 log(2)/-log(1/2)
                         */
                        {
                                3,
                                new double[][] { { 0, 0.25, 0.5, 0, 0.5, 0, 0.25, 0 } },
                                new double[][][] { {
                                        {
                                                Math.pow(Math.log(4.0) / (-Math.log(1.0 / 4.0)), 2),
                                                Math.pow(
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON), 2),
                                                Math.pow(
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON), 2) },
                                        {
                                                Math.pow(
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON), 2),
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2), 0 },
                                        {
                                                Math.pow(
                                                        Math.log(8 * LogBasedCalculation.EPSILON)
                                                                / -Math.log(LogBasedCalculation.EPSILON), 2), 0,
                                                Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2) } } }, "V_nlr(2)", 2 },
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
                                                { 1, 0, 0 },
                                                { 0, Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2),
                                                        Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2) },
                                                { 0, Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2),
                                                        Math.pow(Math.log(3.0 / 2.0) / (-Math.log(2.0 / 3.0)), 2) } },
                                        {
                                                { Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2) },
                                                { Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2) },
                                                { Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 4.0) / -Math.log(1.0 / 3.0), 2),
                                                        Math.pow(Math.log(3.0 / 2.0) / -Math.log(2.0 / 3.0), 2) } },
                                        {
                                                { Math.pow(Math.log(4.0) / (-Math.log(1.0 / 4.0)), 2),
                                                        Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2),
                                                        Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2) },
                                                { Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2),
                                                        Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2), 0 },
                                                { Math.pow(Math.log(2.0) / (-Math.log(1.0 / 4.0)), 2), 0,
                                                        Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2) } },
                                        {
                                                {
                                                        Math.pow(Math.log(4.0) / (-Math.log(1.0 / 4.0)), 2),
                                                        Math.pow(
                                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                                        / -Math.log(LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(
                                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                                        / -Math.log(LogBasedCalculation.EPSILON), 2) },
                                                {
                                                        Math.pow(
                                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                                        / -Math.log(LogBasedCalculation.EPSILON), 2),
                                                        Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2), 0 },
                                                {
                                                        Math.pow(
                                                                Math.log(8 * LogBasedCalculation.EPSILON)
                                                                        / -Math.log(LogBasedCalculation.EPSILON), 2),
                                                        0, Math.pow(Math.log(2.0) / (-Math.log(1.0 / 2.0)), 2) } } },
                                "V_nlr(2)", 2 } });
    }

    public NormalizedLogRatioCalculationBasedCreatorTest(int wordsetSize, double[][] probabilities,
            double[][][] expectedVectors, String expectedCreatorName, double gamma) {
        super(new NormalizedLogRatioConfirmationMeasure(0), wordsetSize, probabilities, expectedVectors,
                expectedCreatorName, gamma);
    }
}
