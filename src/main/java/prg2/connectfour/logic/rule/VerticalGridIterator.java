package prg2.connectfour.logic.rule;

import java.util.ArrayList;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

/**
 * Iterates the grid vertically
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
class VerticalGridIterator implements IGridIterator {
    public Iterable<Cell> getCells(Grid grid, int initialX, int initialY) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (int y = initialY; y < grid.height; ++y) {
            cells.add(grid.getCellAt(initialX, y));
        }

        for (int y = initialY - 1; y >= 0; --y) {
            cells.add(0, grid.getCellAt(initialX, y));
        }

        assert cells.size() == grid.height;

        return cells;
    }
}
