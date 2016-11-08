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
package org.aksw.palmetto.data;

/**
 * This class contains the segmentations and probabilities for a word set.
 * 
 * @author m.roeder
 * 
 */
public class SubsetProbabilities {

    /**
     * Defines the W' of the subset pairs
     */
    public int segments[];
    /**
     * Defines for every W' in {@link #segments} an array of W* subsets.
     */
    public int conditions[][];
    /**
     * The marginal probabilities of the single subsets.
     * 
     * <p>
     * The bits of their index are the IDs of the words that are part of the
     * subset, e.g.,
     * <ul>
     * <li><code>probabilities[0]</code> is always 0</li>
     * <li><code>probabilities[1]</code> contains the probability for word
     * #1</li>
     * <li><code>probabilities[2]</code> contains the probability for word
     * #2</li>
     * <li><code>probabilities[3]</code> contains the probability for a subset
     * comprising word #1 and word #2</li>
     * <li>...</li>
     * </ul>
     * </p>
     */
    public double probabilities[];

    public SubsetProbabilities(int[] segments, int[][] conditions, double[] segmentProbabilities) {
        this.segments = segments;
        this.conditions = conditions;
        this.probabilities = segmentProbabilities;
    }

    /**
     * @return the segmentProbabilities
     */
    public double[] getSegmentProbabilities() {
        return probabilities;
    }

    /**
     * @param segmentProbabilities
     *            the segmentProbabilities to set
     */
    public void setSegmentProbabilities(double[] segmentProbabilities) {
        this.probabilities = segmentProbabilities;
    }
}
