package prg2.connectfour.logic;

import java.util.ArrayList;

import prg2.connectfour.logic.rule.IteratorWinRule;

/**
 * Represents the whole connect-four game
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public class Game {
    public final Grid grid;

    private Player winner;
    private Player[] players;
    private int playerIndex;
    private int playerCount;
    private ArrayList<Integer> turns;

    Game(Player[] players, Grid grid) {
        this.grid = grid;
        this.setPlayers(players);
        this.turns = new ArrayList<>();
    }

    /**
     * Determines if the game is finished
     *
     * @return Game is finished
     */
    public boolean isFinished() {
        return grid.isFull() || this.winner != null;
    }

    /**
     * Returns the winner
     *
     * @return Winner
     */
    public Player getWinner() {
        return this.winner;
    }

    /**
     * Returns the played turns
     *
     * @return Turns
     */
    public ArrayList<Integer> getTurns() {
        return this.turns;
    }

    /**
     * Returns the current active player
     *
     * @return Player
     */
    public Player getActivePlayer() {
        if (isFinished())
            return null;

        return this.players[playerIndex];
    }

    /**
     * Drops a stone for the current active player on the given column
     *
     * @param column Column
     * @return Drop successful
     */
    public boolean dropOnColumn(int column) {
        return dropOnColumn(getActivePlayer(), column);
    }

    /**
     * Sets the players
     *
     * @param players Players
     * @deprecated Use {@link GameFactory}
     */
    @Deprecated
    public void setPlayers(Player[] players) {
        this.grid.replacePlayers(players);
        this.players = players;
        this.playerCount = players.length;
    }

    /**
     * Drops a stone for the given player on the given column
     *
     * @param player Player
     * @param column Column
     * @return Drop successful
     */
    public boolean dropOnColumn(Player player, int column) {
        if (isFinished())
            return false;

        if (getActivePlayer() != player)
            return false;

        if (!grid.dropOnColumn(player, column))
            return false;

        turns.add(column);

        nextPlayer();
        detectWinner();
        return true;
    }

    /**
     * Imports the given turns
     *
     * @param turns Turns
     */
    void importTurns(ArrayList<Integer> turns) {
        for (int turn : turns) {
            boolean ret = this.dropOnColumn(turn);
            assert ret : "Invalid Turn";
        }
    }

    /**
     * Detects and sets the winner
     */
    private void detectWinner() {
        for (IteratorWinRule rule : IteratorWinRule.All) {
            Player winner = rule.playerWon(this.grid);
            if (winner != null) {
                this.winner = winner;
                return;
            }
        }
    }

    /**
     * Cycles to the next player
     */
    private void nextPlayer() {
        this.playerIndex = (this.playerIndex + 1) % this.playerCount;
    }
}
