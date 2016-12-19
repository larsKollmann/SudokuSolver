package de.fhaachen.swegrp2.helper;

import de.fhaachen.swegrp2.controllers.SudokuField;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportExportXML extends ImportExportBase {
    public SudokuField importSudoku(String filename) throws Exception {
        return this.importer.importXML(filename);
    }

    public void exportSudoku(SudokuField field, String filename) throws Exception {
        this.exporter.exportXML(field, filename);
    }
}
