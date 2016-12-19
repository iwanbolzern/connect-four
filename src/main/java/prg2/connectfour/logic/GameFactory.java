package prg2.connectfour.logic;

import java.util.ArrayList;

/**
 * Convenient factory to create a new game with various properties
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public class GameFactory {
    /**
     * Begin creation of a new {@link Game}
     *
     * @return Syntax
     */
    public static GameFactorySyntax create() {
        return new GameFactorySyntax();
    }

    /**
     * GameFactory fluent syntax
     */
    public static class GameFactorySyntax {
        private Player[] players;
        private Grid grid;
        private ArrayList<Integer> turns;

        GameFactorySyntax() {
            this.players = new Player[0];
            this.grid = new Grid(0, 0);
            this.turns = new ArrayList<>();
        }

        /**
         * Adds the given players to the game
         *
         * @param players Players
         * @return Syntax
         */
        public GameFactorySyntax withPlayers(Player... players) {
            this.players = players;
            return this;
        }

        /**
         * Adds the given grid to the game
         *
         * @param grid Grid
         * @return Syntax
         */
        public GameFactorySyntax withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }

        /**
         * Adds the given turns to the game
         *
         * @param turns Turns
         * @return Syntax
         */
        public GameFactorySyntax withTurns(ArrayList<Integer> turns) {
            this.turns = turns;
            return this;
        }

        /**
         * Finishes the creation of the game
         *
         * @return Game
         */
        public Game finish() {
            Game game = new Game(this.players, this.grid);
            game.importTurns(this.turns);
            return game;
        }
    }
}
