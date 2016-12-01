package prg2.connectfour.test.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.logic.*;

public class GameTest {
    private final int GRID_HEIGHT = 5;
    private final int GRID_WIDTH = 10;
    private Game subject;
    private Grid grid;
    private Player[] players;

    @Before
    public void setup() {
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
        this.players = new Player[2];
        this.players[0] = new Player("Player 1", Color.Yellow);
        this.players[1] = new Player("Player 2", Color.Red);
        this.subject = GameFactory
                .create()
                .withGrid(this.grid)
                .withPlayers(this.players)
                .finish();
    }

    @Test
    public void testNewGameNotFinishedNobodyWon() {
        Assert.assertFalse(subject.isFinished());
        Assert.assertNull(subject.getWinner());
    }

    @Test
    public void testCyclePlayers() {
        // temporary
        Player cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(players[0], cur);

        subject.nextPlayer();
        cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(players[1], cur);

        subject.nextPlayer();
        cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(players[0], cur);
    }

    @Test
    public void testPlayerCanMakeMove() {
        Player cur = subject.getActivePlayer();

        boolean result = subject.dropOnColumn(cur, 4);

        Cell cell = grid.getCellAt(4, 0);

        Assert.assertTrue(result);
        Assert.assertEquals(cur, cell.getOwner());
    }

    @Test
    public void testIncorrectPlayerCannotMakeMove() {
        Player prev = subject.getActivePlayer();
        subject.nextPlayer();

        boolean result = subject.dropOnColumn(prev, 4);
        Assert.assertFalse(result);
    }
}
