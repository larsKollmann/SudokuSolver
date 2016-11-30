package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.helper.ImportJSON;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportTestJSON extends ImportTestBase {

    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testimporter = new ImportJSON();
    }

    protected String getFileExtension() {
        return "json";
    }
}
