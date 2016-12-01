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
        if (x < 0 || x >= this.width)
            return null;

        if (y < 0 || y >= this.height)
            return null;

        return cells[getIndex(x, y)];
    }

    public int dropOnColumn(Player player, int column) {
        if (column < 0 || column > this.width)
            return -1;

        for (int y = 0; y < this.height; ++y) {
            int pos = getIndex(column, y);
            if (cells[pos].getOwner() == null) {
                cells[pos].setOwner(player);
                return y;
            }
        }

        return -1;
    }

    private int getIndex(int x, int y) {
        return (this.width * y) + x;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
