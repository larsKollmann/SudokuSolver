package de.fhaachen.swegrp2.models;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Created by simon on 21.11.2016.
 */
public class ImportTest {
    Import testimporter;

    @Before
    public void setUp() throws Exception {
        testimporter = new Import();
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void importXMLTest() throws Exception {
        int[][] testArray = testimporter.importXML("src/test/java/test.xml");

        assertEquals(3, testArray[0][3]);
        assertEquals(7, testArray[3][7]);
        assertNotEquals(5, testArray[6][8]);
    }

    @Test
    public void importCSVTest() throws Exception {
        int[][] testArray = testimporter.importCSV("src/test/java/test.csv");

        assertEquals(3, testArray[0][3]);
        assertEquals(7, testArray[3][7]);
        assertNotEquals(5, testArray[6][8]);
    }

    @Test
    public void importJSONTest() throws Exception {
        int[][] testArray = testimporter.importJSON("src/test/java/test.json");

        assertEquals(3, testArray[0][3]);
        assertEquals(7, testArray[3][7]);
        assertNotEquals(5, testArray[6][8]);
    }
}