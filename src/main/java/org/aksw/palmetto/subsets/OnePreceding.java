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
package org.aksw.palmetto.subsets;

import com.carrotsearch.hppc.BitSet;

public class OnePreceding implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int conditions[][] = new int[wordsetSize][];
        int segments[] = new int[wordsetSize];
        int bit = 1, pos = 0;
        int mask = (1 << wordsetSize) - 1;
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        while (bit < mask) {
            segments[pos] = bit;
            neededCounts.set(bit);
            conditions[pos] = new int[pos];
            for (int i = 0; i < pos; ++i) {
                neededCounts.set(bit + (1 << i));
                conditions[pos][i] = 1 << i;
            }
            bit = bit << 1;
            ++pos;
        }
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S_one-preceding";
    }
}
