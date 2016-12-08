package de.fhaachen.swegrp2.models;

import de.fhaachen.swegrp2.controllers.SudokuField;
import de.fhaachen.swegrp2.models.solver.SudokuGrid;
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

        SudokuGrid grid = SudokuGrid.getGrid(sudokuToSolve.getSudokuField(), sudokuToSolve.getSubFieldSize());
        if(grid.solve()) {
            try {
                sudokuToSolve = new SudokuField(grid.getGridAsIntArr());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        TestHelper.testResult(expectedResult, sudokuToSolve);

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
}
