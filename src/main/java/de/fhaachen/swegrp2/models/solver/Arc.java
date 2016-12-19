package de.fhaachen.swegrp2.models.solver;

/**
 * <p><b>Titel:</b> Arc</p>
 * <p><b>Beschreibung:</b> Für den Solver Algorithmus wichtige Hilfeklasse, für die erfüllung der Constraint Satisfaction.</p>
 */
public class Arc {
	
	private Cell cellx;
	private Cell cellj;

	public Arc(Cell cellx, Cell cellj){
		this.cellx=cellx;
		this.cellj=cellj;
	}

	/**
	 * Vergleicht zwei Arc Objekte auf Gleichheit.
	 * @param arcObj Das zu vergleichende Objekt.
	 * @return Boolean ob die beiden Objekte gleich sind.
	 */
	@Override public boolean equals(Object arcObj) {
		if(!(arcObj instanceof Cell)) return false;
		Arc arc = (Arc)arcObj;
		return this.cellx.equals(arc.cellx) && this.cellj.equals(arc.cellj);
	}

	//Geterfunktionen
	public Cell getCellx() {
		return cellx;
	}
	public Cell getCellj() {
		return cellj;
	}

	//Setterfunktionen
	public void setCellj(Cell cellj) {
		this.cellj = cellj;
	}
	public void setCellx(Cell cellx) {
		this.cellx = cellx;
	}
}
