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

import org.aksw.palmetto.data.SubsetDefinition;
import org.aksw.palmetto.vector.ProbCalcBasedVectorCreator;

import com.carrotsearch.hppc.BitSet;

/**
 * This is just for internal usage by the {@link ProbCalcBasedVectorCreator}
 * class.
 * 
 * @author Micha
 * 
 */
public class OneOneAndSelf implements SegmentationScheme {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int conditions[][] = new int[wordsetSize][wordsetSize];
        int segments[] = new int[wordsetSize];
        int condBit, condPos, bit = 1, pos = 0;
        int mask = (1 << wordsetSize) - 1;
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        while (bit < mask) {
            segments[pos] = bit;
            neededCounts.set(bit);
            condBit = 1;
            condPos = 0;
            if (bit == 1) {
                while (condBit < mask) {
                    neededCounts.set(bit + condBit);
                    conditions[pos][condPos] = condBit;
                    ++condPos;
                    condBit = condBit << 1;
                }
            } else {
                System.arraycopy(conditions[0], 0, conditions[pos], 0, conditions[0].length);
            }
            bit = bit << 1;
            ++pos;
        }
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S^{one}_{o&s}";
    }
}
