package prg2.connectfour.logic.rule;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Reduction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class WinRule {
    // TODO-binarytenshi: cleanup
    public static boolean hasWon(Grid grid) {
        HashSet<Cell> checkedCells = new HashSet<>();

        int width = grid.getWidth();
        int height = grid.getHeight();

        HorizontalGridIterator horizontalIterator = new HorizontalGridIterator();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell cell = grid.getCellAt(x, y);
                if (checkedCells.contains(cell))
                    continue;

                ArrayList<Cell> cells = horizontalIterator.getCells(grid, x, y);
                Stack<Pair<Player, Integer>> stack = reduceCells(cells);

                while (!stack.empty()) {
                    Pair<Player, Integer> pair = stack.pop();
                    if (pair.getRight() >= 4)
                        return true;
                }

                checkedCells.addAll(cells);
            }
        }

        return false;
    }

    private static Stack<Pair<Player, Integer>> reduceCells(ArrayList<Cell> cells) {
        Reduction<Player> reduction = new Reduction<>();

        for (Cell cell : cells) {
            Player player = cell.getOwner();
            if (player == null)
                continue;

            reduction.add(player);
        }

        return reduction.getStack();
    }

    private static class HorizontalGridIterator {
        ArrayList<Cell> getCells(Grid grid, int initialX, int initialY) {
            ArrayList<Cell> cells = new ArrayList<>();
            int width = grid.getWidth();

            for (int x = initialX; x < width; ++x) {
                cells.add(grid.getCellAt(x, initialY));
            }

            for (int x = initialX - 1; x >= 0; --x) {
                cells.add(grid.getCellAt(x, initialY));
            }

            assert cells.size() == width;

            return cells;
        }
    }
}
