package prg2.connectfour.logic;

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
        int pos = getIndex(x, y);
        if (pos < 0 || pos > this.cells.length)
            return null;

        return cells[pos];
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

    private int getIndex(int x, int y) {
        return (this.width * y) + x;
    }
}
