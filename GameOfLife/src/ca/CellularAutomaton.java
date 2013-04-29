package ca;

/**
 * Implementation of a CellularAutomaton.
 * 
 * @author gerardlouw
 */
public final class CellularAutomaton implements Automaton<boolean[][]> {

	private Cell[][] cells;
	/**
	 * Dummy cell representing the dead boudary cell for fixed boundary
	 * conditions.
	 */
	Cell boundary;
	private int rows, columns;

	/**
	 * Constructs a new CellularAutomaton with the specified number of rows and
	 * columns.
	 * 
	 * @param rows
	 *            number of rows
	 * @param columns
	 *            number of columns
	 */
	public CellularAutomaton(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
		cells = new Cell[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				cells[r][c] = new Cell(r, c, this);
		boundary = new Cell(-1, -1, this);
	}

	/**
	 * Returns the number of rows of this CellularAutomaton.
	 * 
	 * @return number of rows
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Returns the number of columns of this CellularAutomaton.
	 * 
	 * @return number of columns
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Returns the Cell at the specified position.
	 * 
	 * @param row
	 *            the row
	 * @param column
	 *            the column
	 * @return Cell at row and column
	 */
	public Cell getCell(int row, int column) {
		return cells[row][column];
	}

	/**
	 * Returns the current state of this CellularAutomaton.
	 * 
	 * @return CelluarAutomaton state
	 */
	@Override
	public boolean[][] getState() {
		boolean[][] state = new boolean[rows][columns];
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				state[r][c] = cells[r][c].getState();
		return state;
	}

	/**
	 * Sets the state of this CellularAutomaton to the specified state.
	 * 
	 * @param state
	 *            the new state of this CellularAutomaton
	 */
	@Override
	public void setState(boolean[][] state) {
		for (int r = 0; r < rows; r++)
			for (int c = 0; c < columns; c++)
				cells[r][c].setState(state[r][c]);
	}

	/**
	 * Returns the state of this CellularAutomaton on acceptance of the
	 * specified AutomatonVisitor.
	 * 
	 * @return CellularAutomaton state
	 */
	@Override
	public boolean[][] accept(AutomatonVisitor visitor) {
		return visitor.visit(this);
	}

	/**
	 * Returns a String representation of this CellularAutomaton, with live
	 * cells represented by '1' and dead cells represented by '0'.
	 * 
	 * @return String representation
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int r = 0; r < rows; r++) {
			for (int c = 0; c < columns; c++)
				sb.append(cells[r][c].getState() ? 1 : 0);
			sb.append('\n');
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
}
