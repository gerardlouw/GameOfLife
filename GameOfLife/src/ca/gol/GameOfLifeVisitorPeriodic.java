package ca.gol;

import ca.AutomatonVisitor;
import ca.Cell;

/**
 * Implementation of the rules of Conways's Game of Life as an AutomatonVisitor,
 * using periodic boundary conditions.
 * 
 * @author gerardlouw
 */
public final class GameOfLifeVisitorPeriodic extends AutomatonVisitor {
	/**
	 * Returns the state of the specified Cell after applying the rules of
	 * Conway's Game of Life, using periodic boundary conditions.
	 * 
	 * @return Cell states
	 */
	@Override
	public boolean visit(Cell cell) {
		int aliveNeighbours = 0;
		for (int r = -1; r <= 1; r++) {
			for (int c = -1; c <= 1; c++) {
				if (r == 0 && c == 0)
					continue;
				Cell neighbour = cell.getNeighbourPeriodic(r, c);
				if (neighbour.getState())
					aliveNeighbours++;
			}
		}
		if (cell.getState())
			return aliveNeighbours == 2 || aliveNeighbours == 3;
		else
			return aliveNeighbours == 3;
	}
}
