package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

import java.util.ArrayList;

class VerticalGridIterator implements IGridIterator {
    public Iterable<Cell> getCells(Grid grid, int initialX, int initialY) {
        ArrayList<Cell> cells = new ArrayList<>();
        int height = grid.getHeight();

        for (int y = initialY; y < height; ++y) {
            cells.add(grid.getCellAt(initialX, y));
        }

        for (int y = initialY - 1; y >= 0; --y) {
            cells.add(grid.getCellAt(initialX, y));
        }

        assert cells.size() == height;

        return cells;
    }
}
