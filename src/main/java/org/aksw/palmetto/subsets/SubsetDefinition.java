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
package org.aksw.palmetto.subsets;

import java.util.Arrays;

import com.carrotsearch.hppc.BitSet;

public class SubsetDefinition {

    public int segments[];
    public int conditions[][];
    public BitSet neededCounts;

    public SubsetDefinition(int[] segments, int[][] conditions,
            BitSet neededCounts) {
        this.segments = segments;
        this.conditions = conditions;
        this.neededCounts = neededCounts;
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
     * @return the neededCounts
     */
    public BitSet getNeededCounts() {
        return neededCounts;
    }

    /**
     * @param neededCounts
     *            the neededCounts to set
     */
    public void setNeededCounts(BitSet neededCounts) {
        this.neededCounts = neededCounts;
    }

    @Override
    public int hashCode() {
        int result = Arrays.hashCode(segments);
        result = 31 * result + Arrays.hashCode(conditions);
        result = 31 * result + ((neededCounts == null) ? 0 : neededCounts.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubsetDefinition other = (SubsetDefinition) obj;
        if (!Arrays.equals(conditions, other.conditions))
            return false;
        if (neededCounts == null) {
            if (other.neededCounts != null)
                return false;
        } else if (!neededCounts.equals(other.neededCounts))
            return false;
        if (!Arrays.equals(segments, other.segments))
            return false;
        return true;
    }
}
