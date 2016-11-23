package de.fhaachen.swegrp2.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by ted on 23.11.2016.
 */
public class TestHelper {

    public static void testResult(SudokuField expected, SudokuField actual) {
        assertEquals(expected.getSize(), actual.getSize());
        for(int i=0; i<expected.getSize(); i++) {
            for(int j=0; j<expected.getSize(); j++) {
                if (expected.getFieldValue(i, j) != actual.getFieldValue(i, j)) {
                    fail("An der Feldstelle [" + i + "][" + j + "]: erwartet: " +
                            expected.getFieldValue(i, j) + " - wirklichkeit: " +
                            actual.getFieldValue(i, j));
                }
            }
        }
    }
}
