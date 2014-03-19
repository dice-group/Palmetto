package org.aksw.palmetto.subsets;

import org.junit.Test;

public class OnePrecedingTest extends AbstractSubsetCreatorTest {

    @Test
    public void testWordSetLength4() {
	int expectedSegments[] = new int[] { 1, 2, 4, 8 };
	int expectedConditions[][] = new int[][] { {}, { 1 }, { 1, 2 },
		{ 1, 2, 4 } };

	testSubsetCreator(new OnePreceding(), expectedSegments,
		expectedConditions);
    }
}
