package prg2.connectfour.logic.rule;

import java.util.Stack;

import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;

/**
 * Iterates the grid and searches for wins.
 * <br>
 * Utilizes the {@link IGridIterator} for iterating.
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public class IteratorWinRule {
    public static IteratorWinRule Horizontal = new IteratorWinRule(IGridIterator.Horizontal);
    public static IteratorWinRule Vertical = new IteratorWinRule(IGridIterator.Vertical);
    public static IteratorWinRule DiagonalRight = new IteratorWinRule(IGridIterator.DiagonalRight);
    public static IteratorWinRule DiagonalLeft = new IteratorWinRule(IGridIterator.DiagonalLeft);

    public static IteratorWinRule[] All = new IteratorWinRule[]{
            Horizontal,
            Vertical,
            DiagonalRight,
            DiagonalLeft
    };

    private IGridIterator iterator;

    private IteratorWinRule(IGridIterator iterator) {
        this.iterator = iterator;
    }

    /**
     * Determines if a player has won on the given grid
     *
     * @param grid Grid
     * @return Winning player
     */
    public Player playerWon(Grid grid) {
        for (int x = 0; x < grid.width; x++) {
            for (int y = 0; y < grid.height; y++) {
                Stack<Pair<Player, Integer>> stack = IteratorReduction.reduceWithIterator(grid, iterator, x, y);

                while (!stack.empty()) {
                    Pair<Player, Integer> pair = stack.pop();
                    if (pair.left == null)
                        continue;

                    if (pair.right >= 4)
                        return pair.left;
                }
            }
        }

        return null;
    }
}
