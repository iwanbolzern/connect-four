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
        int pos = (this.width * y) + x;
        if (pos < 0 || pos > this.cells.length)
            return null;

        return cells[pos];
    }
}
