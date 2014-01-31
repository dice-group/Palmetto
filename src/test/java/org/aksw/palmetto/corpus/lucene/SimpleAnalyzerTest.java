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
