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

import java.util.Arrays;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.aksw.palmetto.webapp.coherences.CoherenceManager;
import org.aksw.palmetto.webapp.config.PalmettoConfiguration;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCoherenceResource extends ServerResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCoherenceResource.class);

    private static final String INDEX_PATH_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.indexPath";
    private static final String MAX_NUMBER_OF_WORDS_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.maxWords";

    protected static final boolean USE_SINGLETON_CORPUS_ADAPTER = true;
    protected static final boolean USE_SINGLE_COHERENCE_INSTANCE = true;
    protected static final String WORD_SEPARATOR = " ";

    public static final String WORDS_ATTRIBUTE_NAME = "words";

    protected String indexPath;
    protected int maxNumberOfWords;

    @Override
    public void init(Context arg0, Request arg1, Response arg2) {
        super.init(arg0, arg1, arg2);
        indexPath = PalmettoConfiguration.getInstance().getString(INDEX_PATH_PROPERTY_KEY);
        if (indexPath == null) {
            String errormsg = "Couldn't load \"" + INDEX_PATH_PROPERTY_KEY + "\" from properties. Aborting.";
            LOGGER.error(errormsg);
            throw new IllegalStateException(errormsg);
        }
        try {
            maxNumberOfWords = PalmettoConfiguration.getInstance().getInt(MAX_NUMBER_OF_WORDS_PROPERTY_KEY);
        } catch (Exception e) {
            String errormsg = "Couldn't load \"" + MAX_NUMBER_OF_WORDS_PROPERTY_KEY + "\" from properties. Aborting.";
            LOGGER.error(errormsg, e);
            throw new IllegalStateException(errormsg, e);
        }
    }

    protected void register() {
        if (USE_SINGLE_COHERENCE_INSTANCE && (CoherenceManager.getInstance().getCoherence(this.getClass()) == null)) {
            CoherenceManager.getInstance().addCoherence(this);
        }
    }

    @Get
    public String represent() {
        String wordList = getAttribute(WORDS_ATTRIBUTE_NAME);
        if (wordList == null) {
            String errMsg = "Couldn't find any words inside the request. You have to add at least 2 words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg + " (list of words is null)";
        }
        String words[] = wordList.split(WORD_SEPARATOR);
        if (words.length < 2) {
            String errMsg = "Couldn't find any words inside the request. You have to add at least 2 words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg + " (list of words is " + Arrays.toString(words) + ")";
        }

        if (words.length > maxNumberOfWords) {
            String errMsg = "Too many words. This web application is restricted to word sets comprising a maximum of "
                    + maxNumberOfWords + " words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg;
        } else {
            try {
                if (USE_SINGLE_COHERENCE_INSTANCE) {
                    Coherence coherence = CoherenceManager.getInstance().getCoherence(this.getClass());
                    return Double.toString(coherence.calculateCoherences(new String[][] { words })[0]);
                } else {
                    return Double.toString(getCoherence(words));
                }
            } catch (Exception e) {
                setStatus(Status.SERVER_ERROR_INTERNAL);
                return "";
            }
        }
    }

    protected abstract double getCoherence(String words[]) throws Exception;

    public abstract Coherence createCoherence(WindowSupportingAdapter corpusAdapter);

    protected static WindowBasedProbabilityEstimator getWindowBasedProbabilityEstimator(int windowSize,
            WindowSupportingAdapter corpusAdapter) {
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(
                        corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return probEstimator;
    }
}
