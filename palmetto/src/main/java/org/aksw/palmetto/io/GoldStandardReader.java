/**
 * Palmetto - Palmetto is a quality measure tool for topics.
 * Copyright © 2014 Data Science Group (DICE) (michael.roeder@uni-paderborn.de)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.aksw.palmetto.io;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotsearch.hppc.DoubleArrayList;

public class GoldStandardReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoldStandardReader.class);

    public static double[] readGoldStandard(String file) throws IOException {
        List<String> lines = FileUtils.readLines(new File(file));

        DoubleArrayList ratings = new DoubleArrayList();
        for (String line : lines) {
            try {
                ratings.add(Double.parseDouble(line));
            } catch (NumberFormatException e) {
                throw new IOException("Error while reading gold standard.", e);
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