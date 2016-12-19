package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

import java.util.ArrayList;

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
