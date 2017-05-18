package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by basti on 08.12.2016.
 *
 * This test should only test 9x9 sudokus.
 * It was copied from the book "Sudoku Jumbo 14".
 */
public class SolverBaseTest {
    private void testBase(String name) throws Exception {
        String pathSudoku = "src/test/resources/solver/task/" + name + ".csv";
        String pathResult = "src/test/resources/solver/result/" + name + ".csv";
        Import importer = new Import();
        SudokuField sudokuToSolve = importer.importCSV(pathSudoku);
        SudokuField expectedResult = importer.importCSV(pathResult);

        SudokuGrid grid = new SudokuGrid(sudokuToSolve);

        if(grid.solve())
                sudokuToSolve = grid.getGridAsSudokuField();
        else
            Assert.fail("Konnte Sudoku nicht lösen");

        TestHelper.testResult(expectedResult, sudokuToSolve);
    }

    /**
     * Testet ein Sudoku und erwartet, dass dieses Sudoku nicht lösbar ist
     * @param name
     * @throws Exception
     */
    private void testNoSolution(String name) throws Exception {
        String pathSudoku = "src/test/resources/solver/task/" + name + ".csv";
        Import importer = new Import();
        SudokuField sudokuToSolve = importer.importCSV(pathSudoku);

        SudokuGrid grid = new SudokuGrid(sudokuToSolve);

        if(grid.solve())
            Assert.fail("Sudoku sollte nicht lösbar sein");
    }

    @Test
    public void easy01() throws Exception {
        testBase("easy_01");
    }

    @Test
    public void easy02() throws Exception {
        testBase("easy_02");
    }

    @Test
    public void easy03() throws Exception {
        testBase("easy_03");
    }

    @Test
    public void easy04() throws Exception {
        testBase("easy_04");
    }

    @Test
    public void easy05() throws Exception {
        testBase("easy_05");
    }

    @Test
    public void advanced01() throws Exception {
        testBase("advanced_01");
    }

    @Test
    public void advanced02() throws Exception {
        testBase("advanced_02");
    }

    @Test
    public void advanced03() throws Exception {
        testBase("advanced_03");
    }

    @Test
    public void advanced04() throws Exception {
        testBase("advanced_04");
    }

    @Test
    public void advanced05() throws Exception {
        testBase("advanced_05");
    }

    @Test
    public void pro01() throws Exception {
        testBase("pro_01");
    }

    @Test
    public void pro02() throws Exception {
        testBase("pro_02");
    }

    @Test
    public void pro03() throws Exception {
        testBase("pro_03");
    }

    @Test
    public void pro04() throws Exception {
        testBase("pro_04");
    }

    @Test
    public void pro05() throws Exception {
        testBase("pro_05");
    }

    @Test
    public void expert01() throws Exception {
        testBase("expert_01");
    }

    @Test
    public void expert02() throws Exception {
        testBase("expert_02");
    }

    @Test
    public void expert03() throws Exception {
        testBase("expert_03");
    }

    @Test
    public void expert04() throws Exception {
        testBase("expert_04");
    }

    @Test
    public void expert05() throws Exception {
        testBase("expert_05");
    }

    @Test
    public void master01() throws Exception {
        testBase("master_01");
    }

    @Test
    public void master02() throws Exception {
        testBase("master_02");
    }

    @Test
    public void master03() throws Exception {
        testBase("master_03");
    }

    @Test
    public void master04() throws Exception {
        testBase("master_04");
    }

    @Test
    public void master05() throws Exception {
        testBase("master_05");
    }

    @Test
    public void NoSolution_DuplicatedColumn01() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn01");
    }

    @Test
    public void NoSolution_DuplicatedColumn02() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn02");
    }

    @Test
    public void NoSolution_DuplicatedColumn03() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn03");
    }

    @Test
    public void NoSolution_DuplicatedColumn04() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn04");
    }

    @Test
    public void NoSolution_DuplicatedColumn05() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn05");
    }

    @Test
    public void NoSolution_DuplicatedColumn06() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn06");
    }

    @Test
    public void NoSolution_DuplicatedColumn07() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn07");
    }

    @Test
    public void NoSolution_DuplicatedColumn08() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn08");
    }

    @Test
    public void NoSolution_DuplicatedColumn09() throws Exception {
        testNoSolution("noSolution_DuplicatedColumn09");
    }

    @Test
    public void NoSolution_DuplicatedRow01() throws Exception {
        testNoSolution("noSolution_DuplicatedRow01");
    }

    @Test
    public void NoSolution_DuplicatedRow02() throws Exception {
        testNoSolution("noSolution_DuplicatedRow02");
    }

    @Test
    public void NoSolution_DuplicatedRow03() throws Exception {
        testNoSolution("noSolution_DuplicatedRow03");
    }

    @Test
    public void NoSolution_DuplicatedRow04() throws Exception {
        testNoSolution("noSolution_DuplicatedRow04");
    }

    @Test
    public void NoSolution_DuplicatedRow05() throws Exception {
        testNoSolution("noSolution_DuplicatedRow05");
    }

    @Test
    public void NoSolution_DuplicatedRow06() throws Exception {
        testNoSolution("noSolution_DuplicatedRow06");
    }

    @Test
    public void NoSolution_DuplicatedRow07() throws Exception {
        testNoSolution("noSolution_DuplicatedRow07");
    }

    @Test
    public void NoSolution_DuplicatedRow08() throws Exception {
        testNoSolution("noSolution_DuplicatedRow08");
    }

    @Test
    public void NoSolution_DuplicatedRow09() throws Exception {
        testNoSolution("noSolution_DuplicatedRow09");
    }

    @Test
    public void NoSolution_DuplicatedField01() throws Exception {
        testNoSolution("noSolution_DuplicatedField01");
    }

    @Test
    public void NoSolution_DuplicatedField02() throws Exception {
        testNoSolution("noSolution_DuplicatedField02");
    }

    @Test
    public void NoSolution_DuplicatedField03() throws Exception {
        testNoSolution("noSolution_DuplicatedField03");
    }

    @Test
    public void NoSolution_DuplicatedField04() throws Exception {
        testNoSolution("noSolution_DuplicatedField04");
    }

    @Test
    public void NoSolution_DuplicatedField05() throws Exception {
        testNoSolution("noSolution_DuplicatedField05");
    }

    @Test
    public void NoSolution_DuplicatedField06() throws Exception {
        testNoSolution("noSolution_DuplicatedField06");
    }

    @Test
    public void NoSolution_DuplicatedField07() throws Exception {
        testNoSolution("noSolution_DuplicatedField07");
    }

    @Test
    public void NoSolution_DuplicatedField08() throws Exception {
        testNoSolution("noSolution_DuplicatedField08");
    }

    @Test
    public void NoSolution_DuplicatedField09() throws Exception {
        testNoSolution("noSolution_DuplicatedField09");
    }

    @Test
    public void emptySudoku() throws Exception {
        String pathSudoku = "src/test/resources/solver/task/emptySudoku.csv";
        Import importer = new Import();
        SudokuField sudokuToSolve = importer.importCSV(pathSudoku);

        SudokuGrid grid = new SudokuGrid(sudokuToSolve);

        if(grid.solve())
            sudokuToSolve = grid.getGridAsSudokuField();
        else
            Assert.fail("Konnte Sudoku nicht lösen");

        int size = sudokuToSolve.getSize();
        Assert.assertEquals(9, size);
        for(int i=0; i<size; i++) {
            for(int k=0; k<size; k++) {
                Assert.assertNotEquals(0, sudokuToSolve.getFieldValue(i,k));
            }
        }
    }
}
