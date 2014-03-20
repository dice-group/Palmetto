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