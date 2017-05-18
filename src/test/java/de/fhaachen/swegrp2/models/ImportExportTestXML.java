package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.helper.ImportExportXML;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportExportTestXML extends ImportExportTestBase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testImporterExporter = new ImportExportXML();
    }

    protected String getFileExtension() {
        return "xml";
    }
}
