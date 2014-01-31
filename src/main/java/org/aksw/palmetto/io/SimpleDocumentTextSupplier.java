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
