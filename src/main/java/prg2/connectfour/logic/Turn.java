package prg2.connectfour.logic;

public class Turn {
    public final int playerIndex;
    public final int column;

    public Turn(int playerIndex, int column) {
        this.playerIndex = playerIndex;
        this.column = column;
    }
}
