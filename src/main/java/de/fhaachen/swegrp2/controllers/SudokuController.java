package de.fhaachen.swegrp2.controllers;
import de.fhaachen.swegrp2.models.*;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

import java.io.File;
import java.util.List;

/**<p><b>Titel:</b> Singleton Sudoku Controller</p>
 * <p><b>Beschreibung:</b> Zentrales Objekt, welches das eigentliche Sudoku, relevante Informationen,
 * sowie weitere Schnittstellen zwischen für die Kommunikation Model und View bietet.</p>
 */
public class SudokuController {
    private static SudokuController ourInstance = new SudokuController();
    private SudokuField sudokuField;    //Speichert das Sudoku und Sudoku relevante Informationen
    private SudokuField previousSudoku; //Das zuletzt erstellte, generierte, importierte Sudoku (zum zuruecksetzen)
    private Import importer;
    private Export exporter;
    private Generator generator;

    //Statische Methode für den Zugriff auf den Singleton Controller
    public static SudokuController getInstance() {
        return ourInstance;
    }
    //Statische Methode zur überprüfung ob eine gegebene Groeße für das Sudoku unterstuetzt wird.
    public static boolean isSizeSupported(int size) {
        return (size == 9 || size == 16 || size == 25 || size == 36);
    }

    private SudokuController () {
        sudokuField = new SudokuField(9); //9 = Standardgroeße
        previousSudoku = sudokuField;
        generator = new Generator();
        importer = new Import();
        exporter = new Export();
    }

    /**
     * Löst das Sudoku und speichert dies im internen Sudokufeld.
     */
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

    /**
     * Generiert ein zufälliges Sudoku und speichert dies im internen Sudokufield.
     */
    public void generate () {
        try {
            generator.generate(sudokuField.getSize());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sudokuField = generator.getSudokuField();
    }

    /**
     * Erhält von der GUI durch den FileChooser ein File Objekt und Importiert die gegebene Datei.
     * @param file Die zu importierende Datei.
     * @throws Exception Warnungen zu korrupten oder sonstig beschädigteten Dateien.
     */
    public void ImportFile(File file) throws Exception{
        String extension = "";
        String filePath = file.getPath();
        int i = filePath.lastIndexOf('.');
        if (i > 0) {
            extension = filePath.substring(i+1);
            if (extension.equals("xml")) {
                sudokuField = importer.importXML(filePath);
            }
            else if (extension.equals("csv")) {
                sudokuField = importer.importCSV(filePath);
            }
            else if (extension.equals("json")) {
                sudokuField = importer.importJSON(filePath);
            }
            else {
                //Fehlermeldung
            }
        }
        else {
            //Fehlermeldung
        }
    }

    /**
     * Liefert eine Liste aller Konfliktbehafteten Felder.
     * @return Liste aller Fehlerhaften felder.
     */
    public List<int[]> getConflicts() {
        return sudokuField.getConflictCoordinates();
    }

    /**
     * Erstellt ein neues mit 0en gefülltes SudokuField.
     */
    public void clear() {
        sudokuField = new SudokuField(sudokuField.getSize());
    }

    /**
     * Erstellt ein neues, mit 0en befülltes SudokuFeld angegebener Groeße.
     * @param size Die groeße des neuen SudokuFields
     */
    public void reset(int size) {
        sudokuField = new SudokuField(size);
    }

    //Getterfunktionen
    public int getSubFieldsize() {
        return (int) Math.sqrt(sudokuField.getSize());
    }
    public int getSize() {
        return sudokuField.getSize();
    }
    public int getFieldValue(int y, int x) {
        return sudokuField.getFieldValue(y, x);
    }
}
