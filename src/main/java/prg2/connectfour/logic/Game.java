package prg2.connectfour.logic;

import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.Color;

public class Game {
    private Grid grid;
    private boolean isFinished;
    private Player[] players;
    private int playerIndex;
    private int playerCount;

    public Game() {
        this.grid = new Grid(10, 5);
        this.isFinished = false;
        this.playerCount = 2;
        this.players = new Player[this.playerCount];

        this.players[0] = new Player("Player 1", Color.Yellow);
        this.players[1] = new Player("Player 2", Color.Red);
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
}