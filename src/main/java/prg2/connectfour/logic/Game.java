package prg2.connectfour.logic;

import prg2.connectfour.logic.rule.IteratorWinRule;

import java.util.ArrayList;

public class Game {
    private Grid grid;
    private Player winner;
    private Player[] players;
    private int playerIndex;
    private int playerCount;
    private ArrayList<Turn> turns;

    Game(Player[] players, Grid grid) {
        this.grid = grid;
        this.playerCount = players.length;
        this.players = players;
        this.turns = new ArrayList<>();
    }

    public boolean isFinished() {
        return grid.isFull() || this.winner != null;
    }

    public Player getWinner() {
        return this.winner;
    }

    public Player getActivePlayer() {
        if (isFinished())
            return null;

        return this.players[playerIndex];
    }

    private void nextPlayer() {
        this.playerIndex = (this.playerIndex + 1) % this.playerCount;
    }

    public boolean dropOnColumn(Player player, int column) {
        if (isFinished())
            return false;

        if (getActivePlayer() != player)
            return false;

        if (!grid.dropOnColumn(player, column))
            return false;

        turns.add(new Turn(playerIndex, column));

        nextPlayer();
        detectWinner();
        return true;
    }

    private void detectWinner() {
        for (IteratorWinRule rule : IteratorWinRule.All) {
            Player winner = rule.playerWon(this.grid);
            if (winner != null) {
                this.winner = winner;
                return;
            }
        }
    }

    public boolean dropOnColumn(int column) {
        return dropOnColumn(getActivePlayer(), column);
    }

    void importTurns(ArrayList<Turn> turns) {
        for (Turn turn : turns) {
            boolean ret = this.dropOnColumn(this.players[turn.playerIndex], turn.column);
            assert ret : "Invalid Turn";
        }
    }

    public ArrayList<Turn> getTurns() {
        return this.turns;
    }

    public int getGridWidth() {
        return this.grid.getWidth();
    }

    public int getGridHeight() {
        return this.grid.getHeight();
    }
}