package de.fhaachen.swegrp2.controllers;

import de.fhaachen.swegrp2.models.ExceptionSuite.EmptyArrayException;
import de.fhaachen.swegrp2.models.ExceptionSuite.SizeNotSupportedException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static de.fhaachen.swegrp2.controllers.SudokuController.isSizeSupported;

/**
 * <p><b>Titel:</b> SudokuField</p>
 * <p><b>Beschreibung:</b> Beinhaltet alle für das System relevanten Informatioenen über das Sudoku.
 * Wrappt das eigentliche Sudoku und bietet zusätzliche Informationen und Methoden zum Umgang dessen.</p>
 */
public class SudokuField {
    private int size;                       //Groeße des Sudoku
    private int subFieldSize;               //Groeße der einzelnen Sudokufelder
    private int fieldValues[][];            //Eigentlichen Werte des Sudoku
    private Boolean cellIsGenerated[][];    //Feld mit Boolschen Werten, die angeben, ob die Zelle vom System generiert wurde

    private Boolean systemGenereated = false;   //gibt an, ob das vorliegende Feld vom System generiert wurde

    /**
     * Konstruktor, initialisiert Parameter.
     * @param size Die Größe des zu erstellenden SudokuField.
     */
    public SudokuField(int size) {
        this.size = size;
        subFieldSize = (int) Math.sqrt(size);
        fieldValues = new int[size][size];

        cellIsGenerated = new Boolean[size][size];
        for(Boolean[] bools : cellIsGenerated) Arrays.fill(bools, false);
    }

    /**
     * Konstruktor, erstellt aus einem gegebenen 2D int Array ein Deep-Copy für das SudokuField.
     * @param arr Das 2D int Array welches das Sudoku darstellt.
     * @throws Exception Wirft diverse Excpetions, wenn das übergebene int Array fehlerbehaftet (ungültig) ist.
     */
    public SudokuField(int arr[][]) throws Exception {
        if (arr == null)
            throw new EmptyArrayException("Kein Array an SudokuField-Konstruktor übergeben");

        if (isSizeSupported(arr.length)) {
            this.size = arr.length;
            this.subFieldSize = (int) Math.sqrt(size);
        } else {
            throw new SizeNotSupportedException("Array ungültiger Größe an SudokuField-Konstrukter übergeben");
        }

        fieldValues = new int[size][];
        for (int i = 0; i < size; i++) {
            fieldValues[i] = Arrays.copyOf(arr[i], arr[i].length);
        }

        cellIsGenerated = new Boolean[size][size];
        for(Boolean[] bools : cellIsGenerated) Arrays.fill(bools, false);

        if(!checkNumbersInRange()) throw new Exception("Zahlen nicht im erlaubten Bereich");
    }

    private boolean isCellValid(int row, int col, int k) {
        for (int ind = 0; ind < size; ind++) {
            if ((row != ind && fieldValues[ind][col] == k) ||
                    (col != ind && fieldValues[row][ind] == k))
                return false;
        }
        int m = (row / subFieldSize) * subFieldSize;
        int n = (col / subFieldSize) * subFieldSize;
        for (int i = m; i < m + subFieldSize; i++)
            for (int j = n; j < n + subFieldSize; j++) {
                if (i == row && j == col) continue;
                if (this.fieldValues[i][j] == k)
                    return false;
            }
        return true;
    }

    private boolean checkNumbersInRange() {
        for(int y = 0; y < size ; y++) {
            for(int x = 0; x < size; x++) {
                if(fieldValues[y][x] < 0 || fieldValues[y][x] > size)
                    return false;
            }
        }
        return true;
    }

    /**
     * Überprüft alle Felder auf ihre Korrektheit und gibt eine Liste von fehlerhaften Feldern aus.
     * @return Liste aller Konfliktbehafteten Felder.
     */
    public List<int[]> getConflictCoordinates () {
        List<int[]> conflictFields = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for(int b = 0; b < size; b++) {
                if (fieldValues[i][b] != 0 && !isCellValid(i,b,fieldValues[i][b])){
                    conflictFields.add(new int[]{i,b});
                }
            }
        }
        return conflictFields;
    }

    /**
     * Setzt für alle gefüllten Felder des Sudokufields den entsprechenden Eintrag in 'cellIsGenerated' auf true.
     */
    void markAsGenerated() {
        systemGenereated = true;
        for(int row = 0; row < size; row++) {
            for(int col = 0; col < size; col++) {
                if(fieldValues[row][col] != 0) {
                    cellIsGenerated[row][col] = true;
                }
            }
        }
    }

    Boolean isEmpty() {
        for(int[] row : fieldValues){
            for(int field : row) {
                if(field != 0)
                    return false;
            }
        }
        return true;
    }

    //Getterfunktionen
    public int getFieldValue(int y, int x) {
        return fieldValues[y][x];
    }
    public int[][] getSudokuField() {
        return this.fieldValues;
    }
    public int getSubFieldSize() {
        return subFieldSize;
    }
    public int getSize() {
        return size;
    }
    Boolean getCellIsGenerated(int y, int x) {
        return cellIsGenerated[y][x];
    }
    Boolean getSystemGenereated() {
        return systemGenereated;
    }

    //Setterfunktionen
    public void setFieldValue(int y, int x, int value) {
        fieldValues[y][x] = value;
    }
}
