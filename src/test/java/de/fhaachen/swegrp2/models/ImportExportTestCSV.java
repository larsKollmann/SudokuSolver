package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.helper.ImportExportCSV;
import org.junit.Test;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportExportTestCSV extends ImportExportTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testImporterExporter = new ImportExportCSV();
    }

    protected String getFileExtension() {
        return "csv";
    }

    @Test
    public void test () throws Exception {
        Import imp = new Import();
        SudokuField field = imp.importCSV("src/test/resources/importExport/CSV/testFine.csv");
    }


    /**
     * Weniger Zeilen als Size
     * Wirft bei Import einer CSV-Datei keine Exceptions
     */
    @Test
    public void test_RowsSmallerThanSize() throws Exception {
        call("testRowsSmallerThanSize");
    }

    /**
     * Weniger Spalten als Size
     * Wirft bei Import einer CSV-Datei keine Exceptions
     */
    @Test
    public void test_ColumnsSmallerThanSize() throws Exception {
        call("testColumnsSmallerThanSize");
    }
}
