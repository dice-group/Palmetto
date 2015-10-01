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

import com.carrotsearch.hppc.IntObjectOpenHashMap;

/**
 * This decorator implements a very simple cache. Note that it has
 * disadvantages, e.g., it will never stop growing. Thus, the
 * {@link org.aksw.palmetto.prob.decorator.FrequencyCachingDeterminerDecorator}
 * should be used instead.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class SimpleFrequencyCachingDeterminerDecorator extends AbstractSlidingWindowFrequencyDeterminerDecorator {

    private IntObjectOpenHashMap<int[]> cache = new IntObjectOpenHashMap<int[]>();

    public SimpleFrequencyCachingDeterminerDecorator(FrequencyDeterminer determiner) {
        super(determiner);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        int wordSetHash;
        String singleWordSet[][] = new String[1][];
        SegmentationDefinition singleDefinition[] = new SegmentationDefinition[1];
        for (int i = 0; i < definitions.length; ++i) {
            wordSetHash = Arrays.hashCode(wordsets[i]);
            if (cache.containsKey(wordSetHash)) {
                countedSubsets[i] = new CountedSubsets(definitions[i].segments, definitions[i].conditions,
                        cache.get(wordSetHash));
            } else {
                singleWordSet[0] = wordsets[i];
                singleDefinition[0] = definitions[i];
                countedSubsets[i] = this.determiner.determineCounts(singleWordSet, singleDefinition)[0];
                cache.put(wordSetHash, countedSubsets[i].counts);
            }
        }
        return countedSubsets;
    }
}
