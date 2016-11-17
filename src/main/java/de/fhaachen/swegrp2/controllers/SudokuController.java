package de.fhaachen.swegrp2.controllers;

/**
 * Created by lars on 17.11.16.
 */
public class SudokuController {
    private static SudokuController ourInstance = new SudokuController();

    public static SudokuController getInstance() {
        return ourInstance;
    }

    private SudokuController() {
    }
}
