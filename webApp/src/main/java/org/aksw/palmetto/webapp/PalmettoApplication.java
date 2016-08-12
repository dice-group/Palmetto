/**
 * This file is part of Palmetto Web Application.
 *
 * Palmetto Web Application is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto Web Application is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto Web Application.  If not, see <http://www.gnu.org/licenses/>.
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

    private static final int GC_TRIGGER = 10;

    protected WindowSupportingAdapter luceneAdapter;
    protected Coherence caCoherence;
    protected Coherence cpCoherence;
    protected Coherence cvCoherence;
    protected Coherence npmiCoherence;
    protected Coherence uciCoherence;
    protected Coherence umassCoherence;
    protected int maxNumberOfWords;
    protected int calcCounts = 0;

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

    protected synchronized void postRequestHandling() {
        ++calcCounts;
        if (calcCounts >= GC_TRIGGER) {
            System.gc();
            calcCounts = 0;
        }
    }

    protected ResponseEntity<String> calculate(String words, Coherence coherence) {
        if (words.equals("")) {
            return new ResponseEntity<String>("The request doesn't contain any words.", HttpStatus.BAD_REQUEST);
        }
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
