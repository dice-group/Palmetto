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
package org.aksw.palmetto.prob.window;

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;

import com.carrotsearch.hppc.IntArrayList;
import com.carrotsearch.hppc.IntIntOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;

public abstract class AbstractWindowBasedFrequencyDeterminer implements WindowBasedFrequencyDeterminer {

      protected WindowSupportingAdapter corpusAdapter;
      protected int windowSize;
      protected long wordSetCountSums[];
    
      public AbstractWindowBasedFrequencyDeterminer(WindowSupportingAdapter corpusAdapter, int windowSize) {
          this.corpusAdapter = corpusAdapter;
          setWindowSize(windowSize);
      }
    
      @Override
      public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
          CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
          for (int i = 0; i < definitions.length; ++i) {
              countedSubsets[i] = new CountedSubsets(definitions[i].segments,
                      definitions[i].conditions, determineCounts(wordsets[i]));
          }
          return countedSubsets;
      }
    
      protected int[] determineCounts(String wordset[]) {
          int counts[] = new int[(1 << wordset.length)];
          IntArrayList positions[];
          IntIntOpenHashMap docLengths = new IntIntOpenHashMap();
          IntObjectOpenHashMap<IntArrayList[]> positionsInDocs = corpusAdapter.requestWordPositionsInDocuments(wordset,
                  docLengths);
          for (int i = 0; i < positionsInDocs.keys.length; ++i) {
              if (positionsInDocs.allocated[i]) {
                  positions = ((IntArrayList[]) ((Object[]) positionsInDocs.values)[i]);
                  addCountsFromDocument(positions, counts, docLengths.get(positionsInDocs.keys[i]));
              }
          }
          return counts;
      }
      
      protected abstract void addCountsFromDocument(IntArrayList[] positions, int[] counts, int docLength);
      
      @Override
      public void setWindowSize(int windowSize) {
          this.windowSize = windowSize;
          determineWordSetCountSum();
      }
    
      @Override
      public long[] getCooccurrenceCounts() {
          return wordSetCountSums;
      }
      
      protected abstract void determineWordSetCountSum();
}
