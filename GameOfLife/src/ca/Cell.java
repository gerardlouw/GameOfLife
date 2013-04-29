package ca;

/**
 * Implementation of Cell automaton, representing a single cell in a
 * CellularAutomaton.
 * 
 * @author gerardlouw
 */
public class Cell implements Automaton<Boolean> {
	private boolean state;
	private int row, column;
	private CellularAutomaton parent;

	/**
	 * Constructs a new cell with the specified row and column indices, and
	 * CellularAutomaton parent.
	 * 
	 * @param row
	 *            the row index
	 * @param column
	 *            the column index
	 * @param parent
	 *            the CellularAutomaton parent
	 */
	public Cell(int row, int column, CellularAutomaton parent) {
		this.row = row;
		this.column = column;
		this.parent = parent;
	}

	/**
	 * Constructs a new cell with the specified row and column indices, initial
	 * state, and CellularAutomaton parent.
	 * 
	 * @param row
	 *            the row index
	 * @param column
	 *            the column index
	 * @param state
	 *            the initial state
	 * @param parent
	 *            the CellularAutomaton parent
	 */
	public Cell(int row, int column, boolean state, CellularAutomaton parent) {
		this(row, column, parent);
		this.state = state;
	}

	/**
	 * Returns the neighbour of this Cell using a relative position with fixed
	 * boundary conditions.
	 * 
	 * @param relativeRow
	 *            row relative to this Cell
	 * @param relativeColumn
	 *            column relative to this Cell
	 * @return the specified neighbour
	 */
	public Cell getNeighbourFixed(int relativeRow, int relativeColumn) {
		int r = row + relativeRow, c = column + relativeColumn;
		if (r < 0 || r >= parent.getRows() || c < 0 || c >= parent.getColumns())
			return parent.boundary;
		return parent.getCell(r, c);
	}

	/**
	 * Returns the neighbour of this Cell using a relative position with
	 * periodic boundary conditions.
	 * 
	 * @param relativeRow
	 *            row relative to this Cell
	 * @param relativeColumn
	 *            column relative to this Cell
	 * @return the specified neighbour
	 */
	public Cell getNeighbourPeriodic(int relativeRow, int relativeColumn) {
		int r = (row + relativeRow) % parent.getRows(), c = (column + relativeColumn)
				% parent.getColumns();
		if (r < 0)
			r += parent.getRows();
		if (c < 0)
			c += parent.getColumns();
		return parent.getCell(r, c);
	}

	/**
	 * Returns the row index of this Cell.
	 * 
	 * @return row index
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column index of this Cell.
	 * 
	 * @return column index
	 */
	public int getColumn() {
		return column;
	}

	/**
	 * Returns the current state of this Cell.
	 * 
	 * @return Cell state
	 */
	@Override
	public Boolean getState() {
		return state;
	}

	/**
	 * Sets the state of this Cell to the specified state.
	 * 
	 * @param state
	 *            the new state of this Cell
	 */
	@Override
	public void setState(Boolean state) {
		this.state = state;
	}

	/**
	 * Returns the state of this Cell on acceptance of the specified
	 * AutomatonVisitor.
	 * 
	 * @return Cell state
	 */
	@Override
	public Boolean accept(AutomatonVisitor visitor) {
		return visitor.visit(this);
	}
}
