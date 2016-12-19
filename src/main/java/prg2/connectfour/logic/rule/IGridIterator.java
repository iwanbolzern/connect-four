package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

/**
 * Iterates the grid on a specified axis
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public interface IGridIterator {
    IGridIterator Horizontal = new HorizontalGridIterator();
    IGridIterator Vertical = new VerticalGridIterator();
    IGridIterator DiagonalRight = new DiagonalRightGridIterator();
    IGridIterator DiagonalLeft = new DiagonalLeftGridIterator();

    IGridIterator[] All = new IGridIterator[]{
            Horizontal,
            Vertical,
            DiagonalRight,
            DiagonalLeft
    };

    /**
     * Returns all cells on the axis at (x,y)
     *
     * @param grid Grid
     * @param x    X coordinate
     * @param y    Y coordinate
     * @return Cells on the axis at (x,y)
     */
    Iterable<Cell> getCells(Grid grid, int x, int y);
}
