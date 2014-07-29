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
package org.aksw.palmetto.corpus.lucene;

import java.util.Arrays;
import java.util.Collection;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SimpleAnalyzerTest {

    @Parameters
    public static Collection<Object[]> data() {
	return Arrays
		.asList(new Object[][] {
			{
				"This is just a simple test text.",
				new String[] { "This", "is", "just", "a",
					"simple", "test", "text" } },
			{
				"This is another text containing the domain aksw.org which shouldn't be changed! But http://aksw.org can be splitted.",
				new String[] { "This", "is", "another", "text",
					"containing", "the", "domain",
					"aksw.org", "which", "shouldn't", "be",
					"changed", "But", "http", "aksw.org",
					"can", "be", "splitted" } },
			{
				"Is it possible to parse this text containing   multiple   whitespaces?",
				new String[] { "Is", "it", "possible", "to",
					"parse", "this", "text", "containing",
					"multiple", "whitespaces" } },
			{
				"This text ends unexpec",
				new String[] { "This", "text", "ends",
					"unexpec" } } });
    }

    private String text;
    private String expectedTokens[];

    public SimpleAnalyzerTest(String text, String expectedTokens[]) {
	this.text = text;
	this.expectedTokens = expectedTokens;
    }

    public void test(boolean lowercase) throws Exception {
	SimpleAnalyzer analyzer = new SimpleAnalyzer(lowercase);
	TokenStream stream = analyzer.tokenStream("test", text);

	CharTermAttribute token;
	int count = 0;
	stream.reset();
	while (stream.incrementToken()) {
	    Assert.assertTrue(count < expectedTokens.length);
	    token = stream.getAttribute(CharTermAttribute.class);
	    if (lowercase) {
		Assert.assertEquals(expectedTokens[count].toLowerCase(),
			token.toString());
	    } else {
		Assert.assertEquals(expectedTokens[count], token.toString());
	    }
	    ++count;
	}
	Assert.assertEquals(expectedTokens.length, count);
	analyzer.close();
    }

    @Test
    public void normalTest() throws Exception {
	test(false);
    }

    @Test
    public void testWithLowerCases() throws Exception {
	test(true);
    }
}
