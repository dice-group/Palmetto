package org.aksw.palmetto;

/**
 * A coherence measure calculates the coherence of a given set of top word sets.
 * 
 * @author Michael Röder
 * 
 */
public interface Coherence {

    /**
     * Calculates the coherence for the given set of top word sets.
     * 
     * @param wordsets
     *            set of topic top words
     * @return a double array containing the coherences for the given top word
     *         sets.
     */
    public double[] calculateCoherences(String[][] wordsets);

    /**
     * Returns the name of the coherence.
     * 
     * @return the name of the coherence
     */
    public String getName();
}
