package prg2.connectfour.logic;

import java.util.ArrayList;

public class GameFactory {
    public static GameFactorySyntax create() {
        return new GameFactorySyntax();
    }

    public static class GameFactorySyntax {
        private Player[] players;
        private Grid grid;
        private ArrayList<Turn> turns;

        GameFactorySyntax() {
            this.players = new Player[0];
            this.grid = new Grid(0, 0);
            this.turns = new ArrayList<>();
        }

        public GameFactorySyntax withPlayers(Player... players) {
            this.players = players;
            return this;
        }

        public GameFactorySyntax withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }

        public GameFactorySyntax withTurns(ArrayList<Turn> turns) {
            this.turns = turns;
            return this;
        }

        public Game finish() {
            Game game = new Game(this.players, this.grid);
            game.importTurns(this.turns);
            return game;
        }
    }
}
