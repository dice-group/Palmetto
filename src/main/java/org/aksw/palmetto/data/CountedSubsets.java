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
 * This class contains the segmentations and word counts for a word set.
 * 
 * @author m.roeder
 * 
 */
public class CountedSubsets {

    public int segments[];
    public int conditions[][];
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
