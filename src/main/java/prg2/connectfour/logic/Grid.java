package prg2.connectfour.logic;

import prg2.connectfour.utils.Pair;

import java.util.Arrays;

/**
 * Represents the grid of the connect-four game
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public class Grid {
    public final int width;
    public final int height;

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

    /**
     * Returns the cell at the given coordinates
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Cell at (x,y)
     */
    public Cell getCellAt(int x, int y) {
        if (x < 0 || x >= this.width)
            return null;

        if (y < 0 || y >= this.height)
            return null;

        return cells[getIndex(x, y)];
    }

    /**
     * 'Drops' the given player on the given column
     *
     * @param player Player
     * @param column Column
     * @return <code>true</code> if the drop was successful; <code>false</code> otherwise
     */
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

    /**
     * Determines if the grid is full
     *
     * @return full
     */
    public boolean isFull() {
        return Arrays.stream(this.cells).allMatch(x -> x.getOwner() != null);
    }

    /**
     * Determines if the grid is empty
     *
     * @return empty
     */
    public boolean isEmpty() {
        return Arrays.stream(this.cells).allMatch(x -> x.getOwner() == null);
    }

    /**
     * Returns the array index of the given position
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @return Array position of (x,y)
     */
    private int getIndex(int x, int y) {
        return (this.width * y) + x;
    }

    /**
     * @param players Players
     * @deprecated Use {@link GameFactory}
     */
    @Deprecated
    public void replacePlayers(Player[] players) {
        Player yellow = players[0].color == Color.Yellow ? players[0] : players[1];
        Player red = players[0].color == Color.Red ? players[0] : players[1];

        for (Cell c : cells) {
            Player owner = c.getOwner();
            if (owner == null)
                continue;

            c.setOwner(owner.color == Color.Yellow ? yellow : red);
        }
    }
}
