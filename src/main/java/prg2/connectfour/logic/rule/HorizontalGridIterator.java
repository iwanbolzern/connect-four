package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

import java.util.ArrayList;

class HorizontalGridIterator implements IGridIterator {
    public Iterable<Cell> getCells(Grid grid, int initialX, int initialY) {
        ArrayList<Cell> cells = new ArrayList<>();
        int width = grid.getWidth();

        for (int x = initialX; x < width; ++x) {
            cells.add(grid.getCellAt(x, initialY));
        }

        for (int x = initialX - 1; x >= 0; --x) {
            cells.add(grid.getCellAt(x, initialY));
        }

        assert cells.size() == width;

        return cells;
    }
}
