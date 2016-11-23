package de.fhaachen.swegrp2.models;

import org.junit.*;


/**
 * Created by simon on 21.11.2016.
 *
 * TODO: Write tests for Exception handling.
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

    private SudokuField getExpectedImportResult() {
        SudokuField field = new SudokuField(9);
        for(int i=0; i<9; i++) {
            for(int j=0; j<9; j++) {
                field.setFieldValue(i, j, j);
            }
        }
        return field;
    }

    @Test
    public void importXMLTest() throws Exception {
        SudokuField testArray = testimporter.importXML("src/test/java/test.xml");
        TestHelper.testResult(getExpectedImportResult(), testArray);
    }

    @Test
    public void importCSVTest() throws Exception {
        SudokuField testArray = testimporter.importCSV("src/test/java/test.csv");
        TestHelper.testResult(getExpectedImportResult(), testArray);
    }

    @Test
    public void importJSONTest() throws Exception {
        SudokuField testArray = testimporter.importJSON("src/test/java/test.json");
        TestHelper.testResult(getExpectedImportResult(), testArray);
    }
}