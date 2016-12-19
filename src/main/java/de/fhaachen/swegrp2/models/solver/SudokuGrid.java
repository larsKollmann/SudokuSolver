package de.fhaachen.swegrp2.models.solver;

import de.fhaachen.swegrp2.controllers.SudokuField;

import java.io.IOException;
import java.util.*;

/**<p><b>Titel:</b> SudokuGrid</p>
 * <p><b>Beschreibung:</b> Beinhaltet das Sudokufeld, weist diesem zusätzliche, für den Solver essentielle Eigenschaften zu
 * und enthält die primären Methoden zum Lösen des Sudoku. </p>
 */
public class SudokuGrid {
    int m_size;
    int m_subRow;
    int m_subCol;
    int m_numNodesExpanded = 0;

    Cell[][] m_grid;
    SudokuField sudokuField;

    /**
     * Konstruktor, intitialisiert Parameter und führt Umwandlung des SudokuField durch.
     * @param sudokuField Das zu lösende Sudoku Feld.
     */
    public SudokuGrid(SudokuField sudokuField) {
        this.sudokuField = sudokuField;
        m_size = sudokuField.getSize();
        m_grid = getGrid(sudokuField);
        m_subRow = sudokuField.getSubFieldSize();
        m_subCol = m_subRow;
        for (Cell[] row : m_grid)
            for (Cell cell : row) {
                if (!cell.solved())
                    this.m_constrainedCells.add(cell);
                else
                    this.setVal(cell, cell.val());
            }
    }

    //Interne Funtkionen
    private Cell[][] getGrid(SudokuField sudokuField) {
        int n = sudokuField.getSize();
        int gridInput[][] = sudokuField.getSudokuField();
        Cell[][] grid = new Cell[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = Cell.getCell(gridInput[i][j], i, j);
            }
        }
        return grid;
    }

    private boolean solveGrid() {
        m_numNodesExpanded++;
        Cell cell = this.getNextUnoccupiedCell();
        if (cell == null) return true;

        for (int val = 1; val <= m_size; val++) {
            if (cell.constraints().contains(val)) continue;
            if (this.valid(cell.row(), cell.col(), val)) {
                int prevVal = cell.val();
                this.m_constrainedCells.remove(cell);
                List<Cell> modifiedCells = setVal(cell, val);
                if (this.solve())
                    return true;
                cell.setVal(prevVal);
                this.m_constrainedCells.add(cell);
                resetVal(modifiedCells, val);
            }
        }
        return false;
    }

    private List<Cell> addConstraints(Cell cell, int k) {

        List<Cell> updatedCells = new ArrayList<Cell>(m_size);
        for (int ind = 0; ind < m_size; ind++) {
            Cell colCell = m_grid[ind][cell.col()];
            if (!colCell.solved() && cell.row() != ind && !colCell.constraints().contains(k)) {
                colCell.constraints().add(k);
                if (!updatedCells.contains(colCell))
                    updatedCells.add(colCell);
            }
            Cell rowCell = m_grid[cell.row()][ind];
            if (!rowCell.solved() && cell.col() != ind && !rowCell.constraints().contains(k)) {
                rowCell.constraints().add(k);
                if (!updatedCells.contains(rowCell))
                    updatedCells.add(rowCell);
            }
        }
        int m = (cell.row() / m_subRow) * m_subRow;
        int n = (cell.col() / m_subCol) * m_subCol;
        for (int i = m; i < m + m_subRow; i++)
            for (int j = n; j < n + m_subCol; j++) {
                if (m_grid[i][j].solved() || (i == cell.row() && j == cell.col())) continue;
                Cell groupCell = m_grid[i][j];
                if (!groupCell.constraints().contains(k)) {
                    groupCell.constraints().add(k);
                    if (!updatedCells.contains(groupCell))
                        updatedCells.add(groupCell);
                }
            }
        return updatedCells;
    }

    private List<Cell> setVal(Cell cell, int val) {
        cell.setVal(val);
        List<Cell> constraints = addConstraints(cell, val);
        return constraints;
    }

    private Set<Cell> m_constrainedCells = new HashSet<Cell>(200);

    private boolean valid(int row, int col, int k) {

        for (int ind = 0; ind < m_size; ind++) {
            if ((row != ind && m_grid[ind][col].val() == k) ||
                    (col != ind && m_grid[row][ind].val() == k))
                return false;
        }
        int m = (row / m_subRow) * m_subRow;
        int n = (col / m_subCol) * m_subCol;
        for (int i = m; i < m + m_subRow; i++)
            for (int j = n; j < n + m_subCol; j++) {
                if (i == row && j == col) continue;
                if (this.m_grid[i][j].val() == k)
                    return false;
            }
        return true;
    }

    private Cell getNextUnoccupiedCell() {
        if (this.m_constrainedCells.size() == 0) return null;
        Cell max = Collections.max(this.m_constrainedCells);
        return max;
    }

    private int numNodesExpanded() {
        return this.m_numNodesExpanded;
    }

    private void resetVal(List<Cell> modifiedCells, int k) {
        for (Cell cell : modifiedCells) {
            cell.constraints().remove(k);
//			updateTree(cell);
        }
    }
    //Interne Funktionen

    /**
     * Gibt das Sudoku als ein String aus.
     * @return Der Sudokustring.
     */
    @Override
    public String toString() {
        StringBuffer buff = new StringBuffer();
        for (Cell[] row : m_grid) {
            for (Cell cell : row)
                buff.append(cell.val() + ",");
            buff.append("\n");
        }
        return buff.toString();
    }

    /**
     * Wandelt das als Cell Array gespeicherte Sudoku in ein 2D integer Array um.
     * @return 2D int Array des Sudoku.
     */
    public int[][] getGridAsIntArr() {
        int arr[][] = new int[m_size][m_size];
        for (int i = 0; i < m_grid.length; i++) {
            for (int b = 0; b < m_grid[i].length; b++) {
                arr[i][b] = m_grid[i][b].m_val;
            }
        }
        return arr;
    }

    /**
     * Wandelt das als Cell Array gespeicherte Sudoku in ein SudokuField um.
     * @return SudokuField des Sudoku.
     * @throws Exception Fehler die bei der erstellung eines SudokuField auftreten können.
     * @see SudokuField
     */
    public SudokuField getGridAsSudokuField() throws Exception {
        return new SudokuField(this.getGridAsIntArr());
    }

    /**
     * Schnittstelle des internen Solver Algorithmus. Wird zum lösen des Sudoku von außen aufgerufen.
     * @return Boolean ob das Sudoku erfolgreich gelöst werden konnte.
     */
    public boolean solve() {
        if(sudokuField.getConflictCoordinates().size() > 0) return false;
        else return solveGrid();
    }
}
