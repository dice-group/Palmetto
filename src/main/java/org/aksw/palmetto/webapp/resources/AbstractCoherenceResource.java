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

import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.prob.window.BooleanSlidingWindowFrequencyDeterminer;
import org.aksw.palmetto.prob.window.WindowBasedProbabilityEstimator;
import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

public abstract class AbstractCoherenceResource extends ServerResource {

    public static final int MAX_NUMBER_OF_WORDS = 10;
    public static final String WORDS_ATTRIBUTE_NAME = "words";
    public static final String WORD_SEPARATOR = " ";
    public static final String INDEX_PATH = "/data/m.roeder/daten/Indexes/wikipedia_bd";

    @Get
    public String represent() {
        String wordList = getAttribute(WORDS_ATTRIBUTE_NAME);
        if (wordList == null) {
            String errMsg = "Couldn't find any words inside the request. You have to add at least 2 words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg + " (wordList="
                    + wordList + ")";
        }
        String words[] = wordList.split(WORD_SEPARATOR);
        if (words.length < 2) {
            String errMsg = "Couldn't find any words inside the request. You have to add at least 2 words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg + " (words=" + Arrays.toString(words) + ")";
        }

        if (words.length > MAX_NUMBER_OF_WORDS) {
            String errMsg = "Too many words. This web application is restricted to word sets comprising a maximum of "
                    + MAX_NUMBER_OF_WORDS + " words.";
            setStatus(Status.CLIENT_ERROR_BAD_REQUEST, errMsg);
            return errMsg;
        } else {
            try {
                return Double.toString(getCoherence(words));
            } catch (Exception e) {
                setStatus(Status.SERVER_ERROR_INTERNAL);
                return "";
            }
        }
    }

    protected abstract double getCoherence(String words[]) throws Exception;

    protected static WindowBasedProbabilityEstimator getWindowBasedProbabilityEstimator(int windowSize,
            WindowSupportingAdapter corpusAdapter) {
        WindowBasedProbabilityEstimator probEstimator = new WindowBasedProbabilityEstimator(
                new BooleanSlidingWindowFrequencyDeterminer(
                        corpusAdapter, windowSize));
        probEstimator.setMinFrequency(WindowBasedProbabilityEstimator.DEFAULT_MIN_FREQUENCY * windowSize);
        return probEstimator;
    }
}
