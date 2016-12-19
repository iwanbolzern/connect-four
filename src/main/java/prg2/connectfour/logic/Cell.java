package prg2.connectfour.logic;

/**
 * Represents a cell on the {@link Grid}
 */
public class Cell {
    private Player owner;

    public Player getOwner() {
        return this.owner;
    }

    void setOwner(Player owner) {
        this.owner = owner;
    }
}
