package de.fhaachen.swegrp2.controllers;
import de.fhaachen.swegrp2.models.*;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

/**
 * Singleton Sudoku Controller:
 * Beinhaltet das eigentliche Sudoku, relevante Informationen,
 * sowie weitere Schnittstellen zwischen Model und View.
 */
public class SudokuController {
    private static SudokuController ourInstance = new SudokuController();
    private SudokuField sudokuField;    //Speichert das Sudoku und Sudoku relevante Informationen
    private SudokuField previousSudoku; //Das zuletzt erstellte, generierte, importierte Sudoku (zum zuruecksetzen)
    private Import importer;
    private Export exporter;
    private Generator generator;

    private SudokuController () {
        sudokuField = new SudokuField(9); //9 = Standardgroeße
        previousSudoku = sudokuField;
        generator = new Generator();
        importer = new Import();
        exporter = new Export();
    }

    public void solve(){

        SudokuGrid grid = new SudokuGrid(sudokuField);
        if(grid.solve()) {
            try {
                sudokuField = grid.getGridAsSudokuField();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void generate () {

        try {
            generator.generate(sudokuField.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sudokuField = generator.getSudokuField();
    }

    public static SudokuController getInstance() {
        return ourInstance;
    }

    public static boolean isSizeSupported(int size) {
        return (size == 9 || size == 16 || size == 25 || size == 36);
    }
}
