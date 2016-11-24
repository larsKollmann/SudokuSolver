package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.helper.ImportCSV;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportTestCSV extends ImportTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testimporter = new ImportCSV();
    }

    protected String getName() {
        return "csv";
    }
}
