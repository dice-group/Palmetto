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