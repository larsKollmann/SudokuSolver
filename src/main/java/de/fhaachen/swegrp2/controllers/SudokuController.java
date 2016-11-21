package de.fhaachen.swegrp2.controllers;
/**
 * Singleton Sudoku Controller:
 * Beinhaltet das "eigentliche Sudoku",
 * sowie Schnittstellen zwischen Model und View zur Bearbeitung des Sudoku.
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
    }

    //Ändert die Größe des Sudoku (Das neue Sudoku ist leer)
    public void changeSize (int size) {
        this.size = size;
        resetSudoku();
    }

    //Setter & Getter
    public int getSize () {return size;}
    public int[][] getSudoku () {return sudokuArr;}
}
