package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.helper.ImportXML;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportTestXML extends ImportTestBase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
        this.testimporter = new ImportXML();
    }

    protected String getName() {
        return "xml";
    }
}
