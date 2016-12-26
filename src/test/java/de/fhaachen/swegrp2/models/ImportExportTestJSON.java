package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.helper.ImportExportJSON;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportExportTestJSON extends ImportExportTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testImporterExporter = new ImportExportJSON();
    }

    protected String getFileExtension() {
        return "json";
    }
}
