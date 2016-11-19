package de.fhaachen.swegrp2.controllers;
/**
 * Singleton Sudoku Controller:
 * Beinhaltet das "eigentliche Sudoku",
 * sowie Schnittstellen zwischen Model und View zur Bearbeitung des Sudoku.
 *
 * Das Sudoku Feld wird dargestellt durch ein simples 2-Dimensionales Array.
 * Dabei werden leere Felder mit 0 gefuellt. Diese Werden spaeter in der View bein Zeichnen des Feldes leer gelassen.
 * Die Koordinaten des Sudoku Array fangen von links-oben an, y wawchst nach unten und x nach rechts.
 * Dabei wird das Array immer wie folgt gelesen:
 * for (int y = 0; y < size; y++)
 *     for (int x = 0; x < size; x++)
 *         sudokuArr[y][x] = 0;
 */

public class SudokuController {
    private static SudokuController ourInstance = new SudokuController();
    public static SudokuController getInstance() {
        return ourInstance;
    }


    private int size;
    private int[][] sudokuArr;

    private SudokuController() {
        size = 9;
        resetSudoku();
    }

    //Löscht das ganze Sudoku (füllt es mit 0-Werten)
    public void resetSudoku () {
        sudokuArr = new int[size][size];
        for (int y = 0; y < size; y++)
            for (int x = 0; x < size; x++)
                sudokuArr[y][x] = 0;
    }

    //Änder die größe des Sudoku (Das neue Sudoku ist leer)
    public void changeSize (int size) {
        this.size = size;
        resetSudoku();
    }

    //Setter & Getter
    public int getSize () {return size;}
    public int[][] getSudoku () {return sudokuArr;}
}
