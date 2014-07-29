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
