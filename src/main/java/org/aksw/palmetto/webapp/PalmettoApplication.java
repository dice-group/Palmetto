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
package org.aksw.palmetto.webapp;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.aksw.palmetto.Coherence;
import org.aksw.palmetto.corpus.WindowSupportingAdapter;
import org.aksw.palmetto.webapp.config.PalmettoConfiguration;
import org.aksw.palmetto.webapp.config.RootConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PalmettoApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalmettoApplication.class);

    private static final String MAX_NUMBER_OF_WORDS_PROPERTY_KEY = "org.aksw.palmetto.webapp.resources.AbstractCoherenceResource.maxWords";

    // private static final String WORDS_REQUEST_PARAMETER_NAME = "words";
    private static final String WORD_SEPARATOR = " ";

    protected WindowSupportingAdapter luceneAdapter;
    protected Coherence caCoherence;
    protected Coherence cpCoherence;
    protected Coherence cvCoherence;
    protected Coherence npmiCoherence;
    protected Coherence uciCoherence;
    protected Coherence umassCoherence;
    protected int maxNumberOfWords;

    public PalmettoApplication() {
        LOGGER.error("started...");
        try {
            maxNumberOfWords = PalmettoConfiguration.getInstance().getInt(MAX_NUMBER_OF_WORDS_PROPERTY_KEY);
        } catch (Exception e) {
            String errormsg = "Couldn't load \"" + MAX_NUMBER_OF_WORDS_PROPERTY_KEY + "\" from properties. Aborting.";
            LOGGER.error(errormsg, e);
            throw new IllegalStateException(errormsg, e);
        }
    }

    @PostConstruct
    public void init() throws Exception {
        luceneAdapter = RootConfig.createLuceneAdapter();
        caCoherence = RootConfig.createCACoherence(luceneAdapter);
        cpCoherence = RootConfig.createCPCoherence(luceneAdapter);
        cvCoherence = RootConfig.createCVCoherence(luceneAdapter);
        npmiCoherence = RootConfig.createNPMICoherence(luceneAdapter);
        uciCoherence = RootConfig.createUCICoherence(luceneAdapter);
        umassCoherence = RootConfig.createUMassCoherence(luceneAdapter);
    }

    @PreDestroy
    public void close() {
        luceneAdapter.close();
    }

    @RequestMapping(value = "ca")
    public ResponseEntity<String> caService(@RequestParam(value = "words") String words) {
        LOGGER.info("CA     words=\"" + words + "\".");
        return calculate(words, caCoherence);
    }

    @RequestMapping(value = "cp")
    public ResponseEntity<String> cpService(@RequestParam(value = "words") String words) {
        LOGGER.info("CP     words=\"" + words + "\".");
        return calculate(words, cpCoherence);
    }

    @RequestMapping(value = "cv")
    public ResponseEntity<String> cvService(@RequestParam(value = "words") String words) {
        LOGGER.info("CV     words=\"" + words + "\".");
        return calculate(words, cvCoherence);
    }

    @RequestMapping(value = "npmi")
    public ResponseEntity<String> npmiService(@RequestParam(value = "words") String words) {
        LOGGER.info("NPMI   words=\"" + words + "\".");
        return calculate(words, npmiCoherence);
    }

    @RequestMapping(value = "uci")
    public ResponseEntity<String> uciService(@RequestParam(value = "words") String words) {
        LOGGER.info("UCI    words=\"" + words + "\".");
        return calculate(words, uciCoherence);
    }

    @RequestMapping(value = "umass")
    public ResponseEntity<String> umassService(@RequestParam(value = "words") String words) {
        LOGGER.info("UMass  words=\"" + words + "\".");
        return calculate(words, umassCoherence);
    }

    // @RequestMapping(value = "/{coherenceType}")
    // public ResponseEntity<String> calculate(@PathVariable String
    // coherenceType,
    // @RequestParam(value = "words") String words) {
    // System.out.println(coherenceType);
    // coherenceType = coherenceType.toLowerCase();
    // if (!coherences.containsKey(coherenceType)) {
    // return new ResponseEntity<String>("Unknown coherence type \"" +
    // coherenceType + "\".",
    // HttpStatus.BAD_REQUEST);
    // }
    // String array[] = words.split(WORD_SEPARATOR);
    // Coherence coherence = coherences.get(coherenceType);
    // if (array.length > maxNumberOfWords) {
    // return new ResponseEntity<String>(
    // "The request contains too many words. This service supports a maximum of
    // \"" + maxNumberOfWords
    // + "\" words.",
    // HttpStatus.BAD_REQUEST);
    // }
    //
    // return new
    // ResponseEntity<String>(Double.toString(coherence.calculateCoherences(new
    // String[][] { array })[0]),
    // HttpStatus.OK);
    // }

    protected ResponseEntity<String> calculate(String words, Coherence coherence) {
        String array[] = words.split(WORD_SEPARATOR);
        if (array.length > maxNumberOfWords) {
            return new ResponseEntity<String>("The request contains too many words. This service supports a maximum of "
                    + maxNumberOfWords + " words.", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<String>(
                    Double.toString(coherence.calculateCoherences(new String[][] { array })[0]), HttpStatus.OK);
        }
    }

    // /**
    // * Creates a root Restlet that will receive all incoming calls.
    // */
    // @Override
    // public Restlet createInboundRoot() {
    //
    // Router router = new Router(getContext());
    // router.attach("/ca", CACoherence.class);
    // router.attach("/cp", CPResource.class);
    // router.attach("/cv", CVResource.class);
    // router.attach("/npmi", NPMIResource.class);
    // router.attach("/uci", UCIResource.class);
    // router.attach("/umass", UMassResource.class);
    //
    // Extractor extractor = new Extractor(getContext(), router);
    // extractor.extractFromQuery(AbstractCoherenceBean.WORDS_ATTRIBUTE_NAME,
    // WORDS_REQUEST_PARAMETER_NAME, true);
    // return extractor;
    // }
}
