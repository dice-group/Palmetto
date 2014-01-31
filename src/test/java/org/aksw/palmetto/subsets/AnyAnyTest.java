package org.aksw.palmetto.subsets;

import org.junit.Test;

public class AnyAnyTest extends SubsetCreatorTest {

    @Test
    public void testWordSetLength4() {
	int expectedSegments[] = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
		12, 13, 14 };
	int expectedConditions[][] = new int[][] { { 2, 4, 8, 6, 10, 12, 14 },
		{ 1, 4, 8, 5, 9, 12, 13 }, { 4, 8, 12 },
		{ 1, 2, 3, 8, 9, 10, 11 }, { 2, 8, 10 }, { 1, 8, 9 }, { 8 },
		{ 1, 2, 3, 4, 5, 6, 7 }, { 2, 4, 6 }, { 1, 4, 5 }, { 4 },
		{ 1, 2, 3 }, { 2 }, { 1 } };

	testSubsetCreator(new AnyAny(), expectedSegments, expectedConditions);
    }
}
