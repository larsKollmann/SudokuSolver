package de.fhaachen.swegrp2.controllers;
import de.fhaachen.swegrp2.models.*;

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
    private Solver solver;

    private SudokuController () {
        sudokuField = new SudokuField(9); //9 = Standardgroeße
        previousSudoku = sudokuField;
        generator = new Generator();
        solver = new Solver();
        importer = new Import();
        exporter = new Export();
    }

    public static SudokuController getInstance() {
        return ourInstance;
    }


    /**Beispiel Dummy funktion damit sich alle vorstellen können was die Aufgabe des SudokuControllers ist
     * weitere Funktionen werden erst implementiert wenn diese benoetigt werden
     *
     * Extrahiert die Dateiendung von dem Datenpfad und ruft die entsprechende Import funktion auf
    *Das ergebnis wird anschließend in dem internen SudokuField gespeichert
    *Aufgerufen von der GUI Explicit von einem JavaFX FileChooser Objekt
    *Exceptions sollten in der oberen ebende (GUI) gefangen uns als fehlermedlung dem user angezeigt werden
    public void ImportFile(File file) throws Exception{
        String extension = "";
        String filePath = file.getPath();
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1);
            if (extension.equals("xml")) {
                field = new SudokuField(importer.importXML(filePath).getField());
            }
            else if (extension.equals("csv")) {
                field = new SudokuField(importer.importCSV(filePath).getField());
            }
            else if (extension.equals("json")) {
                field = new SudokuField(importer.importJSON(filePath).getField());
            }
            else {
                //Fehlermeldung
            }
        }
        else {
            //Fehlermeldung
        }
    }
     */
}
