package org.aksw.palmetto.prob.decorator;

import org.aksw.palmetto.prob.FrequencyDeterminer;

public abstract class AbstractFrequencyDeterminerDecorator implements FrequencyDeterminerDecorator {

    protected FrequencyDeterminer determiner;

    public AbstractFrequencyDeterminerDecorator(FrequencyDeterminer determiner) {
        this.determiner = determiner;
    }

    @Override
    public FrequencyDeterminer getDeterminer() {
        return determiner;
    }

    @Override
    public void setDeterminer(FrequencyDeterminer determiner) {
        this.determiner = determiner;
    }

}
