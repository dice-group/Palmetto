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

import com.carrotsearch.hppc.BitSet;

public class AllAll implements SubsetCreator {

    public SubsetDefinition getSubsetDefinition(int wordsetSize) {
        /*
         * Code the combinations of elements not with ids but with bits. 01 is
         * only the first element, 10 is the second and 11 is the combination of
         * both.
         */
        int mask = (1 << wordsetSize) - 1;
        int conditions[][] = new int[][] { { mask } };
        int segments[] = new int[] { mask };
        BitSet neededCounts = new BitSet(1 << wordsetSize);
        neededCounts.set(mask);
        return new SubsetDefinition(segments, conditions, neededCounts);
    }

    @Override
    public String getName() {
        return "S_all-all";
    }
}
