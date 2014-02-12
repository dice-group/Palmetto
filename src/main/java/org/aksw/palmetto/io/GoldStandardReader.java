package org.aksw.palmetto.io;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cc.mallet.util.FileUtils;

import com.carrotsearch.hppc.DoubleArrayList;

public class GoldStandardReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldStandardReader.class);

    public static double[] readGoldStandard(String file) throws IOException {
        String[] lines = FileUtils.readFile(new File(file));

        DoubleArrayList ratings = new DoubleArrayList();
        for (int i = 0; i < lines.length; ++i) {
            try {
                ratings.add(Double.parseDouble(lines[i]));
            } catch (NumberFormatException e) {
                // nothing to do
            }
        }
        return ratings.toArray();
    }

    public static double[] readGoldStandardSavely(String file) {
        double ratings[] = null;
        try {
            ratings = readGoldStandard(file);
        } catch (IOException e) {
            LOGGER.error("Error while trying to read the gold standard. Returning null.", e);
        }
        return ratings;
    }
}