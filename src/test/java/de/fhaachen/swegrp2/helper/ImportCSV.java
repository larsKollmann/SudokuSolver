package de.fhaachen.swegrp2.helper;

import de.fhaachen.swegrp2.controllers.SudokuField;

/**
 * Created by basti on 24.11.2016.
 */
public class ImportCSV extends ImportBase {
    public SudokuField importSudoku(String filename) throws Exception {
        return this.importer.importCSV(filename);
    }
}
