package org.aksw.palmetto.subsets;

import org.junit.Test;

public class OneAnyTest extends SubsetCreatorTest {

    @Test
    public void testWordSetLength4() {
	int expectedSegments[] = new int[] { 1, 2, 4, 8 };
	int expectedConditions[][] = new int[][] { { 2, 4, 8, 6, 10, 12, 14 },
		{ 1, 4, 8, 5, 9, 12, 13 }, { 1, 2, 3, 8, 9, 10, 11 }, { 1, 2, 3, 4, 5, 6, 7 } };

	testSubsetCreator(new OneAny(), expectedSegments, expectedConditions);
    }
}
