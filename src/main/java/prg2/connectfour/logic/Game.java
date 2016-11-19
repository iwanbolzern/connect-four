package prg2.connectfour.logic;

import prg2.connectfour.logic.Grid;

public class Game {
    private Grid grid;
    private boolean isFinished;

    public Game() {
        this.grid = new Grid(10, 5);
        this.isFinished = false;
    }

    public boolean isFinished() {
        return this.isFinished;
    }

    public Object getWinner() {
        return null;
    }
}