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

import java.util.Arrays;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.FrequencyDeterminer;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public class FrequencyCachingDeterminerDecorator extends AbstractSlidingWindowFrequencyDeterminerDecorator {

    private static final int DEFAULT_CACHE_SIZE = 2000;

    private Cache<CachedWordSet, int[]> cache;

    public FrequencyCachingDeterminerDecorator(FrequencyDeterminer determiner) {
        this(determiner, DEFAULT_CACHE_SIZE);
    }

    public FrequencyCachingDeterminerDecorator(FrequencyDeterminer determiner, int cacheSize) {
        super(determiner);
        cache = CacheBuilder.newBuilder().maximumSize(cacheSize).build();
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        CachedWordSet cacheObj;
        int counts[];
        String singleWordSet[][] = new String[1][];
        SegmentationDefinition singleDefinition[] = new SegmentationDefinition[1];
        for (int i = 0; i < definitions.length; ++i) {
            cacheObj = new CachedWordSet(wordsets[i]);
            counts = cache.getIfPresent(cacheObj);
            if (counts != null) {
                countedSubsets[i] = new CountedSubsets(definitions[i].segments, definitions[i].conditions, counts);
            } else {
                singleWordSet[0] = wordsets[i];
                singleDefinition[0] = definitions[i];
                countedSubsets[i] = this.determiner.determineCounts(singleWordSet, singleDefinition)[0];
                cache.put(cacheObj, countedSubsets[i].counts);
            }
        }
        return countedSubsets;
    }

    protected class CachedWordSet {
        public String words[];

        public CachedWordSet(String[] words) {
            this.words = words;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + Arrays.hashCode(words);
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
            CachedWordSet other = (CachedWordSet) obj;

            return Arrays.equals(words, other.words);
        }
    }
}
