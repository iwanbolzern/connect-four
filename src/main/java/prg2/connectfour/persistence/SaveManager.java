package prg2.connectfour.persistence;

import prg2.connectfour.logic.*;

import java.io.*;
import java.util.ArrayList;

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
        writer.write(game.getGridWidth());
        writer.write(game.getGridHeight());

        ArrayList<Turn> turns = game.getTurns();
        writer.write(turns.size());
        for (Turn turn : turns) {
            writer.write(turn.playerIndex);
            writer.write(turn.column);
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
            ArrayList<Turn> turns = new ArrayList<>();

            int size = reader.read();
            for (int i = 0; i < size; i++) {
                turns.add(new Turn(reader.read(), reader.read()));
            }

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
