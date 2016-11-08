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
 * This class contains the segmentations as defined in the
 * {@link SegmentationDefinition} class and word counts for a word set.
 * 
 * @author m.roeder
 * 
 */
public class CountedSubsets {

    /**
     * Defines the W' of the subset pairs
     */
    public int segments[];
    /**
     * Defines for every W' in {@link #segments} an array of W* subsets.
     */
    public int conditions[][];
    /**
     * The counts of the single subsets.
     * 
     * <p>
     * The bits of their index are the IDs of the words that are part of the
     * subset, e.g.,
     * <ul>
     * <li><code>counts[0]</code> is always 0</li>
     * <li><code>counts[1]</code> contains the count for word #1</li>
     * <li><code>counts[2]</code> contains the count for word #2</li>
     * <li><code>counts[3]</code> contains the count for a subset comprising
     * word #1 and word #2</li>
     * <li>...</li>
     * </ul>
     * </p>
     */
    public int counts[];

    public CountedSubsets(int[] segments, int[][] conditions, int counts[]) {
        this.segments = segments;
        this.conditions = conditions;
        this.counts = counts;
    }

    /**
     * @return the segments
     */
    public int[] getSegments() {
        return segments;
    }

    /**
     * @param segments
     *            the segments to set
     */
    public void setSegments(int[] segments) {
        this.segments = segments;
    }

    /**
     * @return the conditions
     */
    public int[][] getConditions() {
        return conditions;
    }

    /**
     * @param conditions
     *            the conditions to set
     */
    public void setConditions(int[][] conditions) {
        this.conditions = conditions;
    }

    /**
     * @return the counts
     */
    public int[] getCounts() {
        return counts;
    }

    /**
     * @param counts
     *            the counts to set
     */
    public void setCounts(int[] counts) {
        this.counts = counts;
    }

}
