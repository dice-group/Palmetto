/**
 * This file is part of Palmetto.
 *
 * Palmetto is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Palmetto is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Palmetto.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleWordSetReader {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SimpleWordSetReader.class);

    public String[][] readWordSets(String inputFile) {
        List<String[]> topics = new ArrayList<String[]>();
        FileReader reader = null;
        Scanner scanner = null;
        try {
            String[] wordset;
            reader = new FileReader(inputFile);
            scanner = new Scanner(reader);
            while (scanner.hasNextLine()) {
                wordset = parseWordSetFromLine(scanner.nextLine());
                if ((wordset != null) && (wordset.length > 0)) {
                    topics.add(wordset);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Error while creating Index. Aborting.", e);
        } finally {
            if (scanner != null) {
                try {
                    scanner.close();
                } catch (Exception e) {
                }
            } else {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return topics.toArray(new String[topics.size()][]);
    }

    private String[] parseWordSetFromLine(String line) {
        List<String> topic = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(line);
        while ((tokenizer.hasMoreTokens())) {
            String nextToken = tokenizer.nextToken();
            topic.add(nextToken.toLowerCase());
        }
        return topic.toArray(new String[topic.size()]);
    }
}
