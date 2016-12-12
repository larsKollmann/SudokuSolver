package de.fhaachen.swegrp2.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.fhaachen.swegrp2.models.ExceptionSuite.*;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;

import static de.fhaachen.swegrp2.controllers.SudokuController.isSizeSupported;

/**
 * Created by basti on 23.11.2016.
 * <p>
 * Die Klasse beinhaltet alle relevanten Informationen ueber ein
 * Sudoku Feld. Momentan ist es nur ein "Wrapper" fuer ein
 * zweidimensionales Array.
 */
public class SudokuField {
    private int size;                   //Groeße des Sudoku
    private int subFieldSize;           //Groeße der einzelnen Sudokufelder
    private int fieldValues[][];        //Eigentlichen Werte des Sudoku
    private boolean fieldSysGen[][];    //Trifft Aussage ob einzelnes Feld System- o. Usergeneriert
    private boolean isSysGen;           //Allgemeine Aussage über den Generierungstyp des Sudoku

    public SudokuField(int size) {
        this.size = size;
        subFieldSize = (int) Math.sqrt(size);
        resetSudoku();
    }

    /**
     * Copy-Konstruktor mit deep-copy.*
     * TODO: Exceptions bei den checks einbauen
     *
     * @param arr
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

        fieldSysGen = new boolean[size][size];
        isSysGen = false;
    }

    public void resetSudoku() {
        fieldValues = new int[size][size];
        fieldSysGen = new boolean[size][size];
        isSysGen = false;
    }

    public int[][] getSudokuField() {
        return this.fieldValues;
    }

    public int getSubFieldSize() {
        return subFieldSize;
    }


    //Hilfreich zum direkten Testen & Sehen der Importe
    public void print() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                System.out.print(fieldValues[y][x]);
                if (fieldSysGen[y][x])
                    System.out.print("s");

                System.out.print(", ");
            }
            System.out.println();
        }
    }

//TODO: Testen

    public List<int[]> getConflictCoordinates () {
        List<int[]> conflictFields = new ArrayList<int[]>();
        for (int i = 0; i < size; i++) {
            for(int b = 0; b < size; b++) {
                if (fieldValues[i][b] != 0 && !valid(i,b,fieldValues[i][b])){
                    conflictFields.add(new int[]{i,b});
                }
            }
        }
        return conflictFields;
    }


    private boolean valid(int row, int col, int k) {

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

    //Setter & Getter
    public void setFieldValue(int y, int x, int value) {
        fieldValues[y][x] = value;
    }

    public void setFieldSysGen(int y, int x, boolean value) {
        fieldSysGen[y][x] = value;
    }

    public void SetIsSysGen(boolean value) {
        isSysGen = value;
    }

    public boolean getIsSysGen() {
        return isSysGen;
    }

    public int getFieldValue(int y, int x) {
        return fieldValues[y][x];
    }

    public boolean getFieldSysGen(int y, int x) {
        return fieldSysGen[y][x];
    }

    public int getSize() {
        return size;
    }
}
