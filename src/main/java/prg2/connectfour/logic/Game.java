package prg2.connectfour.logic;

import java.util.ArrayList;

public class Game {
    private Grid grid;
    private GameState state;
    private ArrayList<DrawListener> drawHandlers = new ArrayList<>();

    public Game(int x, int y) {
        this.grid = new Grid(x, y);
        this.state = GameState.RED_TURN;
    }

    public void addDrawEventListener(DrawListener handler) {
        this.drawHandlers.add(handler);
    }

    public void draw(Player player, int x, int y) {
        for (DrawListener handler : this.drawHandlers) {
            handler.onDraw(player, x, y);
        }
    }

    public GameState dropOnColumn(Player player, int x) {
        switch (this.state) {
        case RED_TURN:
            if (player == Player.RED) {
                int y = grid.dropOnColumn(player, x);
                if (y > 0) {
                    this.state = GameState.YELLOW_TURN;
                    this.draw(player, x, y);
                }
            }
            break;
        case YELLOW_TURN:
            if (player == Player.YELLOW) {
                int y = grid.dropOnColumn(player, x);
                if (y > 0) {
                    this.state = GameState.RED_TURN;
                    this.draw(player, x, y);
                }
            }
            break;
        }
        return this.state;
    }

    public enum GameState {
        RED_TURN, YELLOW_TURN, RED_WON, YELLOW_WON, DRAW
    }

    public interface DrawListener {
        void onDraw(Player player, int x, int y);
    }
}
