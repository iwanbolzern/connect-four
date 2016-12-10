package prg2.connectfour.logic;

import prg2.connectfour.logic.rule.IGridIterator;
import prg2.connectfour.logic.rule.IteratorReduction;
import prg2.connectfour.utils.Pair;

import java.util.Stack;

public class Grid {
    private int width;
    private int height;

    private Cell[] cells;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;

        int size = width * height;
        cells = new Cell[size];
        for (int i = 0; i < size; ++i) {
            cells[i] = new Cell();
        }
    }

    public Cell getCellAt(int x, int y) {
        if (x < 0 || x >= this.width)
            return null;

        if (y < 0 || y >= this.height)
            return null;

        return cells[getIndex(x, y)];
    }

    public boolean dropOnColumn(Player player, int column) {
        if (column < 0 || column > this.width)
            return false;

        for (int y = 0; y < this.height; ++y) {
            int pos = getIndex(column, y);
            if (cells[pos].getOwner() == null) {
                cells[pos].setOwner(player);
                return true;
            }
        }

        return false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isFull() {
        for (Cell cell : this.cells) {
            if (cell.getOwner() == null)
                return false;
        }

        return true;
    }

    public boolean isEmpty() {
        for (Cell cell : this.cells) {
            if (cell.getOwner() != null)
                return false;
        }

        return true;
    }

    public int countFilledCells(int column) {
        Stack<Pair<Player, Integer>> stack = IteratorReduction.reduceWithIterator(this, IGridIterator.Horizontal, column, 0);
        int count = 0;
        while (!stack.empty()) {
            Pair<Player, Integer> pair = stack.pop();
            if (pair.left != null)
                count += pair.right;
        }

        return count;
    }

    public boolean checkCount(int column, int row, int count, Player player) {
        for (IGridIterator iterator : IGridIterator.All) {
            Stack<Pair<Player, Integer>> stack = IteratorReduction.reduceWithIterator(this, iterator, column, row);
            while (!stack.empty()) {
                Pair<Player, Integer> pair = stack.pop();
                if (pair.left != player)
                    continue;

                if (pair.right == count)
                    return true;
            }
        }

        return false;
    }

    private int getIndex(int x, int y) {
        return (this.width * y) + x;
    }
}
