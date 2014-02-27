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
