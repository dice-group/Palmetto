package org.aksw.palmetto.corpus;

public interface BooleanBigramStatsSupportingAdapter extends CorpusAdapter {

    public int getCount(String word1);

    public double getNumberOfWords();

    public int getCooccurenceCount(String word1, String word2);

    public double getNumberOfCooccurences();
}
