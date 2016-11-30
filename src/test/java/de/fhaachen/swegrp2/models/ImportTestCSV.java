package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.helper.ImportCSV;
import org.junit.Test;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportTestCSV extends ImportTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testimporter = new ImportCSV();
    }

    protected String getFileExtension() {
        return "csv";
    }

    @Test
    public void test () throws Exception {
        Import imp = new Import();
        SudokuField field = imp.importCSV("src/test/resources/importExport/CSV/testFine.csv");
        field.print();
    }
}
