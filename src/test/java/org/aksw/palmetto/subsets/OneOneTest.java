package org.aksw.palmetto.subsets;

import org.junit.Test;

public class OneOneTest extends SubsetCreatorTest {

    @Test
    public void testWordSetLength4() {
	int expectedSegments[] = new int[] { 1, 2, 4, 8 };
	int expectedConditions[][] = new int[][] { { 2, 4, 8 }, { 1, 4, 8 },
		{ 1, 2, 8 }, { 1, 2, 4 } };

	testSubsetCreator(new OneOne(), expectedSegments, expectedConditions);
    }
}
