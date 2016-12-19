package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

import java.util.ArrayList;

class HorizontalGridIterator implements IGridIterator {
    public Iterable<Cell> getCells(Grid grid, int initialX, int initialY) {
        ArrayList<Cell> cells = new ArrayList<>();
        for (int x = initialX; x < grid.width; ++x) {
            cells.add(grid.getCellAt(x, initialY));
        }

        for (int x = initialX - 1; x >= 0; --x) {
            cells.add(0, grid.getCellAt(x, initialY));
        }

        assert cells.size() == grid.width;

        return cells;
    }
}
