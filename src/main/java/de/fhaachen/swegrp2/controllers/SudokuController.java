package de.fhaachen.swegrp2.controllers;
import de.fhaachen.swegrp2.models.*;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

import java.io.File;

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

    public void importFile(File file) throws Exception{
        String extension = "";
        String filePath = file.getPath();
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1).toLowerCase();
            if (extension.equals("xml"))
                sudokuField = importer.importXML(filePath);
            else if (extension.equals("csv"))
                sudokuField = importer.importCSV(filePath);
            else if (extension.equals("json"))
                sudokuField = importer.importJSON(filePath);
            else
                throw new ExceptionSuite.FaultyFormatException("Dateiformat nicht unterstützt!");
        }
        else
            throw new ExceptionSuite.FaultyFormatException("Keine Dateiendung gefunden!");
    }

    public void exportFile(File file) throws Exception {
        String extension = "";
        String filePath = file.getPath();
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1).toLowerCase();
            if (extension.equals("xml"))
                exporter.exportXML(sudokuField, filePath);
            else if (extension.equals("csv"))
                exporter.exportXML(sudokuField, filePath);
            else if (extension.equals("json"))
                exporter.exportXML(sudokuField, filePath);
            else if (extension.equals("pdf")) {

            }
            else
                throw new ExceptionSuite.FaultyFormatException("Dateiformat nicht unterstützt!");
        }
        else
            throw new ExceptionSuite.FaultyFormatException("Keine Dateiendung gefunden!");
    }

    public static boolean isSizeSupported(int size) {
        return (size == 9 || size == 16 || size == 25 || size == 36);
    }

    public int getSubFieldsize() {
        return (int) Math.sqrt(sudokuField.getSize());
    }

    public int getSize() {
        return sudokuField.getSize();
    }

    public int getFieldValue(int y, int x) {
        return sudokuField.getFieldValue(y, x);
    }

    public void reset(int i) {
        sudokuField = new SudokuField(i);
    }
}
