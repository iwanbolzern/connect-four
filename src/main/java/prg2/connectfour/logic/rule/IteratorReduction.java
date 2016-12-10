package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

import java.util.Stack;

public abstract class IteratorReduction {
    public static Stack<Pair<Player, Integer>> reduceWithIterator(Grid grid, IGridIterator iterator, int x, int y) {
        Iterable<Cell> cells = iterator.getCells(grid, x, y);
        return Reduction.reduceList(cells, Cell::getOwner);
    }
}
