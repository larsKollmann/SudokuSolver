package de.fhaachen.swegrp2.models.solver;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.Import;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by lars on 05.12.16.
 */
public class SudokuGridTest {
    @Test
    public void solve25() throws Exception {

        int testSudoku[][] = new int[][] {{0,0,5,10,0,4,0,6,11,0,0,1,17,0,0,0,14,7,0,3,0,0,2,25,19},
                {6,0,22,0,4,9,1,0,24,0,3,0,12,7,0,13,0,2,0,8,7,0,16,0,21},
                {0,24,1,0,9,0,18,0,7,3,8,13,0,2,0,5,0,16,0,0,15,0,11,6,4},
                {12,7,0,0,14,0,0,0,2,0,0,5,0,0,21,0,4,0,6,0,20,1,0,17,9},
                {0,2,13,8,0,21,5,0,16,10,0,0,6,0,4,0,0,24,17,0,3,0,7,0,14},
                {14,12,0,0,0,8,0,0,25,0,5,0,0,0,0,11,0,6,4,0,1,24,17,9,0},
                {9,17,0,0,20,3,7,14,0,18,3,2,0,0,8,0,10,0,0,5,22,0,6,4,15},
                {4,6,11,22,0,20,0,9,17,1,0,7,0,12,3,2,0,0,0,13,5,0,0,21,0},
                {0,23,0,5,10,0,11,0,6,0,1,0,0,0,0,7,3,12,14,0,13,2,25,0,8},
                {19,25,2,0,8,0,0,0,23,0,22,0,0,6,15,0,20,0,9,1,0,7,0,14,3},
                {3,14,0,7,0,13,0,8,19,0,0,23,10,0,5,6,0,4,15,0,24,0,9,0,1},
                {8,19,0,2,13,5,0,0,0,16,0,6,0,4,0,17,1,9,0,24,7,12,14,3,18},
                {10,0,23,16,5,0,6,0,4,11,0,0,20,9,1,12,0,14,0,7,2,25,0,8,0},
                {15,4,6,11,0,1,17,0,9,0,7,12,3,0,18,25,0,19,0,2,16,0,21,0,5},
                {20,9,17,0,1,18,0,3,0,7,2,0,8,0,0,0,0,21,0,0,11,6,4,0,22},
                {1,20,9,0,24,7,0,18,3,0,25,0,13,8,2,21,0,0,5,23,6,4,15,22,0},
                {18,0,14,0,7,2,19,0,8,25,0,0,5,0,16,4,0,15,0,0,17,9,20,0,24},
                {13,8,0,0,0,0,21,5,0,0,6,4,0,15,0,0,24,0,1,17,0,14,0,18,7},
                {5,0,21,0,0,11,0,0,15,6,17,9,1,0,24,14,7,3,18,12,0,0,8,13,2},
                {0,15,4,0,11,0,9,1,0,0,0,0,18,3,7,0,2,8,13,0,23,21,10,5,16},
                {16,0,0,21,0,6,15,0,22,4,0,20,0,1,17,3,12,0,7,14,0,0,0,0,25},
                {2,13,0,19,0,0,0,16,5,0,0,15,0,0,6,20,0,1,24,9,14,3,18,7,0},
                {0,0,3,0,12,0,8,2,13,0,21,0,16,5,0,0,6,0,0,4,9,0,1,24,17},
                {24,1,0,9,17,0,3,0,0,14,0,0,2,0,0,0,23,5,0,0,4,0,0,11,6},
                {0,22,0,4,6,17,0,24,1,9,0,0,7,18,0,8,25,13,0,0,21,10,0,16,23}};

        SudokuField sudokuField = new SudokuField(testSudoku);
        SudokuGrid grid = new SudokuGrid(sudokuField);
        if(grid.solve()) {
            try {
                sudokuField = new SudokuField(grid.getGridAsIntArr());
            } catch (Exception e) {
                fail();
            }
        }


    }

    @Test
    public void solve36() throws Exception {
        Import imp = new Import();
        SudokuField field = imp.importCSV("src/test/resources/size36sudoku.csv");
    }


    @Test
    public void testChecker() throws Exception {
        Import imp = new Import();
        SudokuField field = imp.importCSV("src/test/resources/size36sudoku.csv");
        field.setFieldValue(0,2,32);
        System.out.println(field.getConflictCoordinates().size());

        for (int arr[] : field.getConflictCoordinates()) {
            System.out.println(arr[0] + " " + arr[1]);
        }
    }

}