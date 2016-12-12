package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

import java.util.Random;

/**
 * Created by lars on 17.11.16.
 */
public class Generator {
    private SudokuField field; //Backup field, falls man das generierte Sudoku zuruücksetzen will

    public Generator () {}

    /**
     * Generiert ein zufälliges Sudoku angegebener Größe und speichert dies in seinem Private Attribut zu Backup zwecken
     * @param size größe des zu generierenden Sudoku
     * @return boolean ob Sudoku generiert werden konnte, momentan keine Fehlererkennung eingebaut
     * @throws Exception solver exception
     */
    public boolean generate (int size) throws Exception {
        SudokuGrid grid;
        do {
            field = new SudokuField(size);

            //random row & col & number
            int row = new Random().nextInt(size + 1);
            int col = new Random().nextInt(size + 1);
            int num = new Random().nextInt(size + 1);

            field.setFieldValue(row, col, num);

            grid = new SudokuGrid(this.field);

        } while (!grid.solve());

        this.field = grid.getGridAsSudokuField();

        //anzahl an hinweisen die noch übrig gelassen werden sollen
        int hints = ((size* size)/ 2) - (2 * size);

        //Lösche felder
        for (int i = 0; i < hints; i++) {
            int row = new Random().nextInt(size + 1);
            int col = new Random().nextInt(size + 1);
            field.setFieldValue(row, col, 0);
        }

        return true;
    }

    public SudokuField getSudokuField () {return field;}

}
