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
