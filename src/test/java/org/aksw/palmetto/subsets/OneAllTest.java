package org.aksw.palmetto.subsets;

import org.junit.Test;

public class OneAllTest extends AbstractSubsetCreatorTest {

    @Test
    public void testWordSetLength4() {
	int expectedSegments[] = new int[] { 1, 2, 4, 8 };
	int expectedConditions[][] = new int[][] { { 14 }, { 13 }, { 11 },
		{ 7 } };

	testSubsetCreator(new OneAll(), expectedSegments, expectedConditions);
    }
}
