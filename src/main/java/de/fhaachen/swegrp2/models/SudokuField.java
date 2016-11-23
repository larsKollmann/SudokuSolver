package de.fhaachen.swegrp2.models;

/**
 * Created by basti on 23.11.2016.
 *
 * Die Klasse beinhaltet alle relevanten Informationen ueber ein
 * Sudoku Feld. Momentan ist es nur ein "Wrapper" fuer ein
 * zweidimensionales Array.
 *
 * TODO: Ueberlegen wie wir die gewuenschten Zusatzinformationen speichern.
 * Mit Zusatzinformationen sind Informationen wie "Geaendert vom Benutzer",
 * "Generiert vom System", ....
 * Diese Informationen muessen ebenfalls in die Export/Import-Formate
 * uebernommen werden!
 * Da wird es sogar komplizierter.
 *
 */
public class SudokuField {
    private int fieldValues[][];
    private int size;

    /**
     * Das ist eine art Copy-Konstruktor mit deep-copy.
     * Heisst die Werte werden kopiert und es wird nicht
     * auf den orginalwerten gearbeitet.
     *
     * TODO: Eventuell checks einbauen falls arr == null und co
     *
     * @param arr
     */
    public SudokuField(int arr[][]) {
        this.size = arr.length;
        resetSudoku();
        for(int i=0; i<arr.length; i++) {
            for(int j=0; j<arr.length; j++) {
                fieldValues[i][j] = arr[i][j];
            }
        }
    }

    /*
     * TODO: Eventuell check einbauen, dass size > 0 sein muss? Ansonsten Exception?
     */
    public SudokuField(int size) {
        this.size = size;
        resetSudoku();
    }

    public void resetSudoku() {
        fieldValues = new int[size][size];
    }

    public int getFieldValue(int a, int b) {
        return fieldValues[a][b];
    }

    public void setFieldValue(int a, int b, int value) {
        fieldValues[a][b] = value;
    }

    public int getSize() {
        return size;
    }
}
