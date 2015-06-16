/**
 * The MIT License
 * Copyright (c) 2014 Agile Knowledge Engineering and Semantic Web (AKSW)
							(roeder@informatik.uni-leipzig.de)
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
package org.aksw.palmetto.io;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
public class SimpleDocumentTextSupplier implements DocumentTextSupplier {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleDocumentTextSupplier.class);

    public static SimpleDocumentTextSupplier create(File inputFile) {
        Scanner scanner = null;
        SimpleDocumentTextSupplier supplier = null;
        try {
            scanner = new Scanner(inputFile);
            supplier = new SimpleDocumentTextSupplier(scanner);
        } catch (Exception e) {
            LOGGER.error("Couldn't open input file. Returning null.", e);
        }
        return supplier;
    }

    private Scanner scanner;

    private SimpleDocumentTextSupplier(Scanner scanner) {
        this.scanner = scanner;
    }

    public String getNextDocumentText() {
        String text = null;
        if (scanner != null) {
            if (scanner.hasNext()) {
                text = scanner.nextLine();
            } else {
                scanner.close();
                scanner = null;
            }
        }
        return text;
    }
}
