package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;

interface IGridIterator {
    Iterable<Cell> getCells(Grid grid, int x, int y);
}
