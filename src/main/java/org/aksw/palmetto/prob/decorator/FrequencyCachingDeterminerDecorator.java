/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
							(roeder@informatik.uni-leipzig.de)
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
            if (!Arrays.equals(words, other.words))
                return false;
            return true;
        }
    }
}
