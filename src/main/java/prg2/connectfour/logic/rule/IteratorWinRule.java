package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;

import java.util.Stack;

public class IteratorWinRule {
    public static IteratorWinRule Horizontal = new IteratorWinRule(IGridIterator.Horizontal);
    public static IteratorWinRule Vertical = new IteratorWinRule(IGridIterator.Vertical);
    public static IteratorWinRule DiagonalRight = new IteratorWinRule(IGridIterator.DiagonalRight);
    public static IteratorWinRule DiagonalLeft = new IteratorWinRule(IGridIterator.DiagonalLeft);

    private IGridIterator iterator;

    private IteratorWinRule(IGridIterator iterator) {
        this.iterator = iterator;
    }

    public Player playerWon(Grid grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
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
