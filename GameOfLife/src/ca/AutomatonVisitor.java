package ca;

/**
 * Abstract parent of all rules for an Automaton.
 * 
 * @author gerardlouw
 */
public abstract class AutomatonVisitor {
	/**
	 * Returns the state of a Cell after being visited by this AutomatonVisitor.
	 * Uses a rule specified by the subclass.
	 * 
	 * @param cell
	 *            the Cell to visit
	 * @return the state of Cell after visitation
	 */
	public abstract boolean visit(Cell cell);

	/**
	 * Returns the state of a CellularAutomaton after being visited by this
	 * AutomatonVisitor. The returned state is a boolean lattice constructed by
	 * visiting each Cell in the specified CellularAutomaton and storing the
	 * result in the appropriate position.
	 * 
	 * @param ca
	 *            the CellularAutomaton to visit
	 * @return the state of CellularAutomaton after visitation
	 */
	public boolean[][] visit(CellularAutomaton ca) {
		boolean[][] state = new boolean[ca.getRows()][ca.getColumns()];
		for (int r = 0; r < ca.getRows(); r++)
			for (int c = 0; c < ca.getColumns(); c++) {
				state[r][c] = ca.getCell(r, c).accept(this);
			}
		return state;
	}
}
