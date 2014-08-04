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

import org.aksw.palmetto.DirectConfirmationBasedCoherence;
import org.aksw.palmetto.Palmetto;
import org.aksw.palmetto.aggregation.ArithmeticMean;
import org.aksw.palmetto.calculations.direct.LogCondProbConfirmationMeasure;
import org.aksw.palmetto.corpus.CorpusAdapter;
import org.aksw.palmetto.corpus.lucene.LuceneCorpusAdapter;
import org.aksw.palmetto.prob.bd.BooleanDocumentProbabilitySupplier;
import org.aksw.palmetto.subsets.OnePreceding;

public class UMassResource extends AbstractCoherenceResource {

    @Override
    protected double getCoherence(String[] words) throws Exception {
        CorpusAdapter corpusAdapter;
        try {
            corpusAdapter = LuceneCorpusAdapter.create(INDEX_PATH, Palmetto.DEFAULT_TEXT_INDEX_FIELD_NAME);
        } catch (Exception e) {
            throw new IOException("Couldn't open lucene index. Aborting.", e);
        }
        if (corpusAdapter == null) {
            throw new IOException("Couldn't open lucene index. Aborting.");
        }

        DirectConfirmationBasedCoherence coherence = new DirectConfirmationBasedCoherence(new OnePreceding(),
                BooleanDocumentProbabilitySupplier.create(corpusAdapter, "bd", true),
                new LogCondProbConfirmationMeasure(), new ArithmeticMean());

        double result = coherence.calculateCoherences(new String[][] { words })[0];
        corpusAdapter.close();
        return result;
    }

}
