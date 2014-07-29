/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.aksw.palmetto.prob.decorator;

import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedFrequencyDeterminer;

public abstract class AbstractSlidingWindowFrequencyDeterminerDecorator extends AbstractFrequencyDeterminerDecorator
        implements SlidingWindowFrequencyDeterminerDecorator {

    public AbstractSlidingWindowFrequencyDeterminerDecorator(FrequencyDeterminer determiner) {
        super(determiner);
    }

    @Override
    public void setWindowSize(int windowSize) {
        if (determiner instanceof WindowBasedFrequencyDeterminer) {
            ((WindowBasedFrequencyDeterminer) determiner).setWindowSize(windowSize);
        }
    }

    @Override
    public long[] getCooccurrenceCounts() {
        if (determiner instanceof WindowBasedFrequencyDeterminer) {
            return ((WindowBasedFrequencyDeterminer) determiner).getCooccurrenceCounts();
        } else {
            return null;
        }
    }

    @Override
    public String getSlidingWindowModelName() {
        if (determiner instanceof WindowBasedFrequencyDeterminer) {
            return ((WindowBasedFrequencyDeterminer) determiner).getSlidingWindowModelName();
        } else {
            return null;
        }
    }

    @Override
    public int getWindowSize() {
        if (determiner instanceof WindowBasedFrequencyDeterminer) {
            return ((WindowBasedFrequencyDeterminer) determiner).getWindowSize();
        } else {
            return 0;
        }
    }

}
