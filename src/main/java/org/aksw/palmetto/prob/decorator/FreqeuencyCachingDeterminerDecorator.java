package org.aksw.palmetto.prob.decorator;

import java.util.Arrays;

import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.aksw.palmetto.subsets.CountedSubsets;
import org.aksw.palmetto.subsets.SubsetDefinition;

import com.carrotsearch.hppc.IntObjectOpenHashMap;

public class FreqeuencyCachingDeterminerDecorator extends AbstractFrequencyDeterminerDecorator {

    private IntObjectOpenHashMap<int[]> cache = new IntObjectOpenHashMap<int[]>();

    public FreqeuencyCachingDeterminerDecorator(FrequencyDeterminer determiner) {
        super(determiner);
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SubsetDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        int wordSetHash;
        String singleWordSet[][] = new String[1][];
        SubsetDefinition singleDefinition[] = new SubsetDefinition[1];
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
