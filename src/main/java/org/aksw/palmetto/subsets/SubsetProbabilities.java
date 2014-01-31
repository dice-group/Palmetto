package org.aksw.palmetto.subsets;

public class SubsetProbabilities {

    public double segmentProbabilities[];
    public double conditionProbabilities[][];

    public SubsetProbabilities(double[] segmentProbabilities,
	    double[][] conditionProbabilities) {
	this.segmentProbabilities = segmentProbabilities;
	this.conditionProbabilities = conditionProbabilities;
    }

    /**
     * @return the segmentProbabilities
     */
    public double[] getSegmentProbabilities() {
	return segmentProbabilities;
    }

    /**
     * @param segmentProbabilities
     *            the segmentProbabilities to set
     */
    public void setSegmentProbabilities(double[] segmentProbabilities) {
	this.segmentProbabilities = segmentProbabilities;
    }

    /**
     * @return the conditionProbabilities
     */
    public double[][] getConditionProbabilities() {
	return conditionProbabilities;
    }

    /**
     * @param conditionProbabilities
     *            the conditionProbabilities to set
     */
    public void setConditionProbabilities(double[][] conditionProbabilities) {
	this.conditionProbabilities = conditionProbabilities;
    }
}
