package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Iterates the grid diagonally to the upper left
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
class DiagonalLeftGridIterator implements IGridIterator {
    @Override
    public Iterable<Cell> getCells(Grid grid, int initialX, int initialY) {
        ArrayList<Cell> cells = new ArrayList<>();
        int maxIterations = Math.min(grid.width, grid.height);

        cells.add(grid.getCellAt(initialX, initialY));

        int x = initialX;
        int y = initialY;
        for (int i = 0; i < maxIterations; ++i) {
            x += 1;
            y -= 1;
            cells.add(grid.getCellAt(x, y));
        }

        x = initialX;
        y = initialY;
        for (int i = 0; i < maxIterations; ++i) {
            x -= 1;
            y += 1;
            cells.add(0, grid.getCellAt(x, y));
        }

        cells.removeIf(Objects::isNull);

        assert cells.size() <= maxIterations;

        return cells;
    }
}
