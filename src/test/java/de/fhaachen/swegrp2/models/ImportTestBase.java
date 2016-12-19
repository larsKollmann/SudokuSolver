package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.helper.ImportBase;
import org.junit.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;


/**
 * Created by simon on 21.11.2016.
 *
 * TODO: Write tests for Exception handling.
 */
public abstract class ImportTestBase {
    ImportBase testimporter;


    @Before
    public void setUp() throws Exception {

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

    /*
        Returns "csv" for csv, "xml" for xml, "json" for json and so on.
     */
    protected abstract String getFileExtension();

    SudokuField call(String filename) throws Exception {
        String fileextension = getFileExtension();
        String path = "src/test/resources/importExport/" + fileextension + "/" + filename + "." + fileextension;
        return testimporter.importSudoku(path);
    }

    private void exportAndImportAgain() throws Exception {
        String fileExtension = getFileExtension();
        String filename = UUID.randomUUID().toString();
        String path = "src/test/resources/importExport/" + "tmp/";
        new File(path).mkdirs();
        path = path + filename + "." + fileExtension;

        SudokuField exportSudoku = getExpectedImportResult();


        testimporter.exportSudoku(exportSudoku, path);
        try {
            SudokuField importResult = testimporter.importSudoku(path);
            TestHelper.testResult(getExpectedImportResult(), importResult);
        } finally {
            new File(path).delete();
        }
    }

    /**
     * Gutfall. Lese Datei ein und pruefe die Ergebnisse.
     * @throws Exception
     */
    @Test
    public void test_Fine() throws Exception {
        SudokuField testArray = call("testFine");
        TestHelper.testResult(getExpectedImportResult(), testArray);
    }

    /**
     * Es existiert keine Datei mit diesem Namen.
     */
    @Test(expected=FileNotFoundException.class)
    public void test_FileNotFound() throws Exception {
        call("testNotFound");
    }

    /**
     * Die Anzahl Zeilen/Spalten/groeße ist nicht valide.
     */
    @Test(expected=ExceptionSuite.SizeNotSupportedException.class)
    public void test_UnsupportedSize() throws Exception {
        call("testUnsupportedSize");
    }

    /**
     * Mehr Zeilen als Size
     */
    @Test(expected=ExceptionSuite.FaultyFormatException.class)
    public void test_RowsBiggerThanSize() throws Exception {
        call("testRowsBiggerThanSize");
    }

    /**
     * Weniger Zeilen als Size
     */
    @Test(expected=ExceptionSuite.FaultyFormatException.class)
    public void test_RowsSmallerThanSize() throws Exception {
        call("testRowsSmallerThanSize");
    }

    /**
     * Mehr Spalten als Size
     */
    @Test(expected=ExceptionSuite.FaultyFormatException.class)
    public void test_ColumnsBiggerThanSize() throws Exception {
        call("testColumnsBiggerThanSize");
    }

    /**
     * Weniger Spalten als Size
     */
    @Test(expected=ExceptionSuite.FaultyFormatException.class)
    public void test_ColumnsSmallerThanSize() throws Exception {
        call("testColumnsSmallerThanSize");
    }

    @Test
    public void test_ExportAndImportAgain() throws Exception {
        exportAndImportAgain();
    }

}