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
package org.aksw.palmetto.subsets;

import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;

import com.carrotsearch.hppc.BitSet;

/**
 * This is just for internal usage by the {@link DirectConfirmationBasedVectorCreator}
 * class.
 * 
 * @author Micha
 * 
 */
public class OneOneAndSelf implements Segmentator {

    public SegmentationDefinition getSubsetDefinition(int wordsetSize) {
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
        return new SegmentationDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S^{one}_{o&s}";
    }
}
