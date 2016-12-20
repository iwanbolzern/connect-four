package prg2.connectfour.test.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.GameFactory;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

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

        subject.dropOnColumn(cur, 1);
        cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(players[1], cur);

        subject.dropOnColumn(cur, 1);
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
        Player two = this.players[1];

        boolean result = subject.dropOnColumn(two, 4);
        Assert.assertFalse(result);
    }

    @Test
    public void testFullGame() {
        Player one = this.players[0];
        Player two = this.players[1];

        Assert.assertEquals(one, subject.getActivePlayer());
        Assert.assertTrue(subject.dropOnColumn(one, 1));
        Assert.assertTrue(subject.dropOnColumn(two, 2));
        Assert.assertTrue(subject.dropOnColumn(one, 1));
        Assert.assertTrue(subject.dropOnColumn(two, 2));
        Assert.assertTrue(subject.dropOnColumn(one, 1));
        Assert.assertTrue(subject.dropOnColumn(two, 2));
        Assert.assertTrue(subject.dropOnColumn(one, 1));

        // Player one won
        Assert.assertFalse(subject.dropOnColumn(two, 2));
        Assert.assertEquals(one, subject.getWinner());
    }

    /**
     * Test related to bug #9
     * https://github.com/iwanbolzern/connect-four/issues/9
     */
    @Test
    public void testBug9() {
        Player one = this.players[0];

        subject.dropOnColumn(3);
        subject.dropOnColumn(0);
        subject.dropOnColumn(2);
        subject.dropOnColumn(1);
        subject.dropOnColumn(5);
        subject.dropOnColumn(4);
        subject.dropOnColumn(3);
        subject.dropOnColumn(6);
        subject.dropOnColumn(2);
        subject.dropOnColumn(0);
        subject.dropOnColumn(1);
        subject.dropOnColumn(4);
        subject.dropOnColumn(3);
        subject.dropOnColumn(5);
        subject.dropOnColumn(3);

        Assert.assertTrue(subject.isFinished());
        Assert.assertEquals(one, subject.getWinner());
    }
}
