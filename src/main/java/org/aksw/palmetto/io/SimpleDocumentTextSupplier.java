/**
 * Copyright (C) 2014 Michael Röder (michael.roeder@unister.de)
 *
 * Licensed under the Creative Commons Attribution-NonCommercial 4.0
 * International Public License (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://creativecommons.org/licenses/by-nc/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, a file
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.aksw.palmetto.io;

import java.io.File;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
