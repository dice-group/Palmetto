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
package org.aksw.palmetto.calculations.vectorbased;

public class CosinusBasedCalculation extends AbstractVectorBasedCalculation {

    @Override
    public String getCalculationName() {
        return "m_cos";
    }

    @Override
    protected double calculateSimilarity(double[] vector1, double[] vector2) {
        double length1 = 0, length2 = 0, sum = 0;
        for (int i = 0; i < vector1.length; ++i) {
            sum += vector1[i] * vector2[i];
            length1 += Math.pow(vector1[i], 2);
            length2 += Math.pow(vector2[i], 2);
        }
        if ((length1 > 0) && (length2 > 0)) {
            return sum / (Math.sqrt(length1) * Math.sqrt(length2));
        } else {
            if ((length1 == 0) && (length2 == 0)) {
                return 1;
            } else {
                return 0;
            }
        }
    }

}
