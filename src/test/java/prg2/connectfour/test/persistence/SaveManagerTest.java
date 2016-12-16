package prg2.connectfour.test.persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.logic.*;
import prg2.connectfour.persistence.SaveManager;

import java.io.*;

public class SaveManagerTest {
    private final int GRID_HEIGHT = 5;
    private final int GRID_WIDTH = 10;
    private File save;
    private SaveManager subject;

    @Before
    public void setup() throws IOException {
        save = File.createTempFile(".connect-four-save", "test");
        subject = new SaveManager(() -> save);
    }

    @Test
    public void testSavesGame() throws IOException {
        Player p1 = new Player("Player 1", Color.Red);
        Player p2 = new Player("Player 2", Color.Yellow);
        Grid grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
        Game game = GameFactory
                .create()
                .withGrid(grid)
                .withPlayers(p1, p2)
                .finish();

        game.dropOnColumn(4);
        game.dropOnColumn(4);
        game.dropOnColumn(4);

        try {
            subject.save(game);
        } catch (IOException e) {
            Assert.fail(e.toString());
        }

        Assert.assertTrue(save.exists());
        int size = 2 + 1 + 3; // width, height + turn count + turns
        char[] content = new char[size];
        FileReader reader = new FileReader(save);
        int read = reader.read(content);
        reader.close();

        Assert.assertEquals(read, size);
        Assert.assertArrayEquals(content, new char[]{10, 5, 3, 4, 4, 4});
        Assert.assertTrue(save.delete());
    }

    @Test
    public void testLoadsGame() throws IOException {
        Player p1 = new Player("Player 1", Color.Red);
        Player p2 = new Player("Player 2", Color.Yellow);

        char[] content = {
                10,
                5,
                7,
                1, 2, 1, 2, 1, 2, 1
        };

        FileWriter writer = new FileWriter(save);
        writer.write(content);
        writer.close();

        Assert.assertTrue(subject.hasSave());

        Game game = subject.load(p1, p2);

        Assert.assertFalse(save.exists());
        Assert.assertTrue(game.isFinished());
        Assert.assertEquals(p1, game.getWinner());
    }
}
