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
package org.aksw.palmetto.webapp.resources;

import java.io.IOException;

import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.VectorBasedCoherence;
import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.calculations.direct.NormalizedLogRatioConfirmationMeasure;
import org.aksw.palmetto.calculations.indirect.CosinusConfirmationMeasure;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.corpus.lucene.WindowSupportingLuceneCorpusAdapter;
import org.aksw.palmetto.prob.window.ContextWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.subsets.OneOne;
import org.aksw.palmetto.vector.DirectConfirmationBasedVectorCreator;
import org.aksw.palmetto.webapp.config.PalmettoConfiguration;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CAResource extends AbstractCoherenceResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CAResource.class);

    private static final int DEFAULT_WINDOW_SIZE = 5;
    private static final String WINDOW_SIZE_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.CAResource.windowSize";

    private int windowSize;

    @Override
    public void init(Context arg0, Request arg1, Response arg2) {
        super.init(arg0, arg1, arg2);
        try {
            windowSize = PalmettoConfiguration.getInstance().getInt(WINDOW_SIZE_PROPERTY_KEY);
        } catch (Exception e) {
            LOGGER.warn("Couldn't load \"{}\" from properties. Using default window size={}.",
                    WINDOW_SIZE_PROPERTY_KEY, DEFAULT_WINDOW_SIZE);
            windowSize = DEFAULT_WINDOW_SIZE;
        }
    }

    @Override
    protected double getCoherence(String[] words) throws Exception {
        WindowSupportingAdapter corpusAdapter;
        try {
            corpusAdapter = WindowSupportingLuceneCorpusAdapter.create(this.indexPath,
                    Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME, Palmetto.DEFAULT_DOCUMENT_LENGTH_INDEX_FIELD_NAME);
        } catch (Exception e) {
            throw new IOException("Couldn't open lucene index. Aborting.", e);
        }
        if (corpusAdapter == null) {
            throw new IOException("Couldn't open lucene index. Aborting.");
        }

        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new ContextWindowFrequencyDeterminer(corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        VectorBasedCoherence coherence = new VectorBasedCoherence(new OneOne(),
                new DirectConfirmationBasedVectorCreator(probEstimator, new NormalizedLogRatioConfirmationMeasure()),
                new CosinusConfirmationMeasure(), new ArithmeticMean());

        double result = coherence.calculateCoherences(new String[][] { words })[0];
        corpusAdapter.close();
        return result;
    }

}
