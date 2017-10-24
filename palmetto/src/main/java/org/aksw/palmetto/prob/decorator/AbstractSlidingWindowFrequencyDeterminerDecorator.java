/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
