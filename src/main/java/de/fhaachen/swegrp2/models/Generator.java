package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

import java.util.Random;

/**
 * <p><b>Titel:</b> Generator</p>
 * <p><b>Beschreibung:</b> Generiert ein zufälliges Sudoku angebebener Größe
 * und speichert dies für weitere externe Zugriffe.</p>
 */
public class Generator {
    private SudokuField field;

    /**
     * Generiert ein zufälliges Sudoku angegebener Größe und speichert dies in seinem Private Attribut zu Backup zwecken
     * @param size größe des zu generierenden Sudoku
     * @return boolean ob Sudoku generiert werden konnte, momentan keine Fehlererkennung eingebaut
     * @throws Exception solver exception
     */
    public boolean generate (int size) throws Exception {
        SudokuGrid grid;

        field = new SudokuField(size);

        //random row & col & number
        int row = new Random().nextInt(size);
        int col = new Random().nextInt(size);
        int num = new Random().nextInt(size);

        field.setFieldValue(row, col, num);

        grid = new SudokuGrid(this.field);

        grid.solve();

        field = grid.getGridAsSudokuField();

        //Anzahl an Hinweisen die gelöscht werden sollen
        int hints = ((size* size)/ 2) - (3 * size);

        //Lösche Felder
        for (int i = 0; i < hints; i++) {
            int rowtodel = new Random().nextInt(size);
            int coltodel = new Random().nextInt(size);
            field.setFieldValue(rowtodel, coltodel, 0);
        }

        return true;
    }

    //Getterfuntkionen
    public SudokuField getSudokuField () {return field;}
}
