package org.aksw.palmetto.subsets;

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
}
