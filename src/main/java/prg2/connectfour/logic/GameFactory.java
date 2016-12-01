package prg2.connectfour.logic;

public class GameFactory {
    public static GameFactorySyntax create() {
        return new GameFactorySyntax();
    }

    public static class GameFactorySyntax {
        private Player[] players;
        private Grid grid;

        GameFactorySyntax() {
            this.players = new Player[0];
            this.grid = new Grid(0, 0);
        }

        public GameFactorySyntax withPlayers(Player... players) {
            this.players = players;
            return this;
        }

        public GameFactorySyntax withGrid(Grid grid) {
            this.grid = grid;
            return this;
        }

        public Game finish() {
            return new Game(this.players, this.grid);
        }
    }
}
