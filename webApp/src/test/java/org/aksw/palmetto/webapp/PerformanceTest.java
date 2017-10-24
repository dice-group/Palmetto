/**
 * Palmetto Web Application - Palmetto is a quality measure tool for topics.
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
package org.aksw.palmetto.webapp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.webapp.config.RootConfig;
import org.apache.commons.io.FileUtils;

/**
 * A simple performance test used to find memory leaks.
 * 
 * @author Michael R&ouml;der (roeder@informatik.uni-leipzig.de)
 *
 */
public class PerformanceTest {

    private static final String VOCAB_FILE = "../Palmetto.coocmatrix/vocab.tsv";

    private static final int NUMBER_OF_WORDSETS = 10000;
    private static final int NUMBER_OF_TERMS = 10;

    public static void main(String[] args) throws Exception {
        PerformanceTest test = new PerformanceTest();
        test.run();
    }

    public void run() throws Exception {
        WindowSupportingAdapter luceneAdapter = null;
        try {
            luceneAdapter = RootConfig.createLuceneAdapter();
            Coherence coherence = RootConfig.createUCICoherence(luceneAdapter);

            Random random = new Random(System.currentTimeMillis());
            String terms[] = loadTerms();

            System.out.println("Waiting 20 secs...");
            Thread.sleep(20000);
            
            String words[] = new String[NUMBER_OF_TERMS];
            for (int i = 0; i < NUMBER_OF_WORDSETS; ++i) {
                if ((i % 1000) == 0) {
                    System.out.println("Starting word set #" + i);
                }
                for (int j = 0; j < words.length; ++j) {
                    words[j] = terms[random.nextInt(terms.length)];
                }
                coherence.calculateCoherences(new String[][] { words });
            }
        } finally {
            if (luceneAdapter != null) {
                luceneAdapter.close();
            }
        }
    }

    protected String[] loadTerms() throws IOException {
        List<String> lines = FileUtils.readLines(new File(VOCAB_FILE), "UTF-8");
        return lines.toArray(new String[lines.size()]);
    }
}
