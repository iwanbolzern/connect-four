package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

import java.util.Stack;

public class IteratorWinRule {
    public static IteratorWinRule Horizontal = new IteratorWinRule(new HorizontalGridIterator());
    public static IteratorWinRule Vertical = new IteratorWinRule(new VerticalGridIterator());
    public static IteratorWinRule DiagonalRight = new IteratorWinRule(new DiagonalRightGridIterator());
    public static IteratorWinRule DiagonalLeft = new IteratorWinRule(new DiagonalLeftGridIterator());

    private IGridIterator iterator;

    private IteratorWinRule(IGridIterator iterator) {
        this.iterator = iterator;
    }

    public Player playerWon(Grid grid) {
        int width = grid.getWidth();
        int height = grid.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Iterable<Cell> cells = iterator.getCells(grid, x, y);
                Stack<Pair<Player, Integer>> stack = Reduction.reduceList(cells, Cell::getOwner);

                while (!stack.empty()) {
                    Pair<Player, Integer> pair = stack.pop();
                    if (pair.getLeft() == null)
                        continue;

                    if (pair.getRight() >= 4)
                        return pair.getLeft();
                }
            }
        }

        return null;
    }
}
