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
package org.aksw.palmetto.prob.decorator;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.aksw.palmetto.data.CountedSubsets;
import org.aksw.palmetto.data.SegmentationDefinition;
import org.aksw.palmetto.prob.FrequencyDeterminer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.BitSet;

@RunWith(Parameterized.class)
public class FrequencyCachingDeterminerDecoratorTest implements FrequencyDeterminer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FrequencyCachingDeterminerDecoratorTest.class);

    private static final int NUMBER_OF_TEST_INSTANCES = 1000;
    private static final int NUMBER_OF_REQUESTS = 100000;

    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { { new SimpleFrequencyCachingDeterminerDecorator(null) },
                { new FrequencyCachingDeterminerDecorator(null) } });
    }

    private Map<WordSet, int[]> values = new HashMap<WordSet, int[]>();
    private Map<WordSet, int[]> notRequested = new HashMap<WordSet, int[]>();
    private FrequencyDeterminerDecorator cache;
    private Random rand;

    public FrequencyCachingDeterminerDecoratorTest(FrequencyDeterminerDecorator cache) {
        this.cache = cache;
        cache.setDeterminer(this);
        String words[];
        int counts[];
        rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < NUMBER_OF_TEST_INSTANCES; ++i) {
            words = new String[rand.nextInt(10) + 1];
            for (int j = 0; j < words.length; ++j) {
                words[j] = Integer.toString(rand.nextInt());
            }
            Arrays.sort(words);
            if (!values.containsKey(words)) {
                counts = new int[words.length];
                for (int j = 0; j < counts.length; ++j) {
                    counts[j] = rand.nextInt();
                }
                values.put(new WordSet(words), counts);
                notRequested.put(new WordSet(words.clone()), counts);
            }
        }
    }

    @Test
    public void test() {
        LOGGER.info("Testing cache...");
        String wordsets[][];
        SegmentationDefinition[] definitions;
        SegmentationDefinition definition = new SegmentationDefinition(new int[0], new int[0][0], new BitSet(0));
        CountedSubsets result[];
        for (int i = 0; i < NUMBER_OF_REQUESTS; ++i) {
            wordsets = new String[rand.nextInt(5) + 1][];
            definitions = new SegmentationDefinition[wordsets.length];
            for (int j = 0; j < wordsets.length; ++j) {
                wordsets[j] = chooseWordSet();
                definitions[j] = definition;
            }
            result = cache.determineCounts(wordsets, definitions);
            Assert.assertEquals(wordsets.length, result.length);
            for (int j = 0; j < result.length; ++j) {
                Assert.assertArrayEquals(values.get(new WordSet(wordsets[j])), result[j].counts);
            }
        }
    }

    @Override
    public CountedSubsets[] determineCounts(String[][] wordsets, SegmentationDefinition[] definitions) {
        CountedSubsets countedSubsets[] = new CountedSubsets[definitions.length];
        WordSet wordSetObj;
        for (int i = 0; i < definitions.length; ++i) {
            wordSetObj = new WordSet(wordsets[i]);
            Assert.assertTrue("The following word set is unknown or has been requested before: \"" + wordsets[i]
                    + "\".", notRequested.containsKey(wordSetObj));
            countedSubsets[i] = new CountedSubsets(definitions[i].segments, definitions[i].conditions,
                    notRequested.get(wordSetObj));
            notRequested.remove(wordSetObj);
        }
        return countedSubsets;
    }

    protected String[] chooseWordSet() {
        Iterator<WordSet> iterator = values.keySet().iterator();
        int chosenTestInstance = rand.nextInt(NUMBER_OF_TEST_INSTANCES);
        while (chosenTestInstance > 0) {
            iterator.next();
            --chosenTestInstance;
        }
        return iterator.next().words;
    }

    protected class WordSet {
        public String words[];

        public WordSet(String[] words) {
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
            WordSet other = (WordSet) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (!Arrays.equals(words, other.words))
                return false;
            return true;
        }

        private FrequencyCachingDeterminerDecoratorTest getOuterType() {
            return FrequencyCachingDeterminerDecoratorTest.this;
        }

    }
}
