package de.fhaachen.swegrp2.models.solver;

import java.util.HashSet;
import java.util.Set;

/**<p><b>Titel:</b> Cell</p>
 * <p><b>Beschreibung:</b> Bietet für ein einzelnes Sudokufeld für den Solver essentielle Daten.</p>
 */
public class Cell implements Comparable<Cell> {
    private static final int DEF = 0; //Konstante zur Bestimmung des Status des Feldes (Geloest/ Ungeloest)
    private Set<Integer> mConstraints = new HashSet<>();
    private int mRow, mCol;
    int mVal;
    /**
     * Statische Methode die zu gegebenen Wert, Zeile, Spalte eine neue Cell erstellt.
     * @param c Wert des Feldes.
     * @param row Zeile in dem sich das Feld befindet.
     * @param col Spalte in dem sich das Feld befindet.
     * @return Die neu erstellte Cell.
     */
    static Cell getCell(int c, int row, int col) {
        if (0 == c)
            return new Cell(DEF, row, col);
        else
            return new Cell(c, row, col);
    }

    private int constraintSize() {
        return mConstraints.size();
    }

    private Cell(int val, int row, int col) {
        mVal = val;
        mRow = row;
        mCol = col;
    }

    /**
     * Gibt die Cell als String aus.
     * @return Das Stringojekt welches die Cell darstellt.
     */
    @Override
    public String toString() {
        return "" + mVal + "[" + mRow + "," + mCol + "]";
    }

    /**
     * Methode zum Vergleich auf Constraint Satisfaction mit einem anderen Cell Objekt.
     * @param cell2 Das Objekt das es zu vergleichen gilt.
     * @return Boolean ob Cell Objekt die Constraint Satisfaction erfüllt.
     */
    @Override
    public int compareTo(Cell cell2) {
        if (this.constraintSize() == cell2.constraintSize())
            return 1;
        return (this.constraintSize() - cell2.constraintSize()) * 10000;// - this.constraintSize();
    }

    /**
     * Vergleicht zwei Cell Objekte auf Gleichheit.
     * @param cellObj Die zu vergleichende Zelle.
     * @return Boolean ob die beiden Cell Objekte gleich sind.
     */
    @Override
    public boolean equals(Object cellObj) {
        if (!(cellObj instanceof Cell)) return false;
        Cell cell = (Cell) cellObj;
        return cell.row() == mRow && cell.col() == mCol;
    }

    //Getterfunktionen
    int row() {
        return mRow;
    }
    int col() {
        return mCol;
    }
    int val() {return mVal;}
    Set<Integer> constraints() {return mConstraints;}

    /**
     * Fragt ab ob das einzelne Feld im geloesten Zustand ist.
     * @return Boolean wert ob das Feld geloest wurde.
     */
    boolean solved() {
        return mVal != DEF;
    }

    //Setterfunktionen
    void setVal(int m_val) {this.mVal = m_val;}
}
