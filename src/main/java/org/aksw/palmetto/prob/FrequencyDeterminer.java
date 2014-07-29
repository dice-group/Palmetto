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
package org.aksw.palmetto.prob;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

/**
 * Determines the frequencies of words and word sub sets of a given word set.
 * 
 * @author m.roeder
 * 
 */
public interface FrequencyDeterminer {

    /**
     * Returns the frequencies of words and word sub sets of the given word sets.
     * 
     * @param wordsets
     * @param definitions
     * @return
     */
    public CountedSubsets[] determineCounts(String wordsets[][],
            SegmentationDefinition definitions[]);
}
