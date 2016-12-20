package prg2.connectfour.persistence;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.GameFactory;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class SaveManager {
    private ISaveFileProvider saveFileProvider;

    public SaveManager(ISaveFileProvider saveFileProvider) {
        this.saveFileProvider = saveFileProvider;
    }

    public boolean hasSave() {
        return this.saveFileProvider.get().exists();
    }

    public boolean save(Game game) throws IOException {
        File file = this.saveFileProvider.get();
        if (!file.exists())
            file.createNewFile();

        FileWriter writer = new FileWriter(file, false);
        writer.write(game.grid.width);
        writer.write(game.grid.height);

        ArrayList<Integer> turns = game.getTurns();
        writer.write(turns.size());
        for (int turn : turns) {
            writer.write(turn);
        }

        writer.close();

        return true;
    }

    public Game load(Player... players) {
        if (!hasSave()) {
            throw new IllegalStateException("no save available");
        }

        File file = this.saveFileProvider.get();

        FileReader reader;
        try {
            reader = new FileReader(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            assert false : "FileReader file not found. Should not happen.";
            return null;
        }

        try {
            int width = reader.read();
            int height = reader.read();
            ArrayList<Integer> turns = new ArrayList<>();

            int size = reader.read();
            for (int i = 0; i < size; i++) {
                turns.add(reader.read());
            }

            reader.close();

            Grid grid = new Grid(width, height);
            return GameFactory
                    .create()
                    .withPlayers(players)
                    .withGrid(grid)
                    .withTurns(turns)
                    .finish();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            file.delete();
        }
    }
}
