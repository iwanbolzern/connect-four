package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

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

    Iterable<Cell> getCells(Grid grid, int x, int y);
}
