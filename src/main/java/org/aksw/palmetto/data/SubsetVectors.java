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
package org.aksw.palmetto.data;

/**
 * This class contains the segmentations, probabilities and context vectors for a word set.
 * 
 * @author m.roeder
 * 
 */
public class SubsetVectors extends SubsetProbabilities {

    public double vectors[][];

    public SubsetVectors(int[] segments, int[][] conditions, double[][] vectors, double[] segmentProbabilities) {
        super(segments, conditions, segmentProbabilities);
        this.vectors = vectors;
    }
}
