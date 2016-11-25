package de.fhaachen.swegrp2.controllers;
import de.fhaachen.swegrp2.models.*;

import java.io.File;

/**
 * Singleton Sudoku Controller:
 * Beinhaltet das eigentliche Sudoku,
 * sowie Schnittstellen zwischen Model und View zur Bearbeitung des Sudoku.
 */

public class SudokuController {
    private SudokuField field;
    Import importer;
    Export exporter;
    Generator generator;
    Solver solver;

    private static SudokuController ourInstance = new SudokuController();
    public static SudokuController getInstance() {
        return ourInstance;
    }
    private SudokuController () {
        field = new SudokuField(9); //9 = Standardgroeße
        importer = new Import();
        exporter = new Export();
        generator = new Generator();
        solver = new Solver();
    }

    //Extrahiert die Dateiendung von dem Datenpfad und ruft die entsprechende Import funktion auf
    //Das ergebnis wird anschließend in dem internen SudokuField gespeichert
    //Aufgerufen von der GUI Explicit von einem JavaFX FileChooser Objekt
    //Exceptions sollten in der oberen ebende (GUI) gefangen uns als fehlermedlung dem user angezeigt werden
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

    public void ExportFile (File file) {

    }
}
