package org.aksw.palmetto.prob.decorator;

import org.aksw.palmetto.prob.FrequencyDeterminer;

public interface FrequencyDeterminerDecorator extends FrequencyDeterminer {

    public FrequencyDeterminer getDeterminer();

    public void setDeterminer(FrequencyDeterminer determiner);
}
