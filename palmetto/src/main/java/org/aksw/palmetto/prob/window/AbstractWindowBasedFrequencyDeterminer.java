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
