package de.fhaachen.swegrp2.helper;

import de.fhaachen.swegrp2.models.Import;
import de.fhaachen.swegrp2.models.SudokuField;

/**
 * Created by basti on 24.11.2016.
 */
public abstract class ImportBase {
    protected final Import importer = new Import();
    public abstract SudokuField importSudoku(String filename) throws Exception;
}
