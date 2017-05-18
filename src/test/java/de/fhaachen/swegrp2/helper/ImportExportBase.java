package de.fhaachen.swegrp2.helper;

import de.fhaachen.swegrp2.models.Export;
import de.fhaachen.swegrp2.models.Import;
import de.fhaachen.swegrp2.controllers.SudokuField;

/**
 * Created by basti on 24.11.2016.
 */
public abstract class ImportExportBase {
    protected final Import importer = new Import();
    protected final Export exporter = new Export();
    public abstract SudokuField importSudoku(String filename) throws Exception;
    public abstract void exportSudoku(SudokuField field, String filename) throws Exception;
}
