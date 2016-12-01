package prg2.connectfour.logic;

public class Game {
    private Grid grid;
    private boolean isFinished;
    private Player[] players;
    private int playerIndex;
    private int playerCount;

    Game(Player[] players, Grid grid) {
        this.grid = grid;
        this.isFinished = false;
        this.playerCount = players.length;
        this.players = players;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public Player getWinner() {
        return null;
    }

    public Player getActivePlayer() {
        return this.players[playerIndex];
    }

    public void nextPlayer() {
        this.playerIndex = (this.playerIndex + 1) % this.playerCount;
    }

    public boolean dropOnColumn(Player player, int column) {
        if (getActivePlayer() != player)
            return false;

        return grid.dropOnColumn(player, column);
    }
}