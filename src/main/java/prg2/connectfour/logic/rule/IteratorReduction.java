package prg2.connectfour.logic.rule;

import java.util.Stack;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

/**
 * Reduces the cells returned by an {@link IGridIterator}
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public abstract class IteratorReduction {
    /**
     * Reduces the cells returned by the given iterator
     *
     * @param grid     Grid
     * @param iterator Iterator
     * @param x        X coordinate
     * @param y        Y coordinate
     * @return Reduced cell stack
     */
    public static Stack<Pair<Player, Integer>> reduceWithIterator(Grid grid, IGridIterator iterator, int x, int y) {
        Iterable<Cell> cells = iterator.getCells(grid, x, y);
        return Reduction.reduceList(cells, Cell::getOwner);
    }
}
