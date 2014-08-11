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

import org.aksw.palmetto.webapp.resources.AbstractCoherenceResource;
import org.aksw.palmetto.webapp.resources.CPResource;
import org.aksw.palmetto.webapp.resources.CVResource;
import org.aksw.palmetto.webapp.resources.NPMIResource;
import org.aksw.palmetto.webapp.resources.UCIResource;
import org.aksw.palmetto.webapp.resources.UMassResource;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Extractor;
import org.restlet.routing.Router;

public class PalmettoRestletApplication extends Application {

    public static final String WORDS_REQUEST_PARAMETER_NAME = "words";

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
    @Override
    public Restlet createInboundRoot() {
        Router router = new Router(getContext());
        router.attach("/uci", UCIResource.class);
        router.attach("/npmi", NPMIResource.class);
        router.attach("/umass", UMassResource.class);
        router.attach("/cv", CVResource.class);
        router.attach("/cp", CPResource.class);
        router.attachDefault(new SimpleResourceRetriever());

        Extractor extractor = new Extractor(getContext(), router);
        extractor.extractFromQuery(AbstractCoherenceResource.WORDS_ATTRIBUTE_NAME, WORDS_REQUEST_PARAMETER_NAME, true);
        return extractor;
    }
}
