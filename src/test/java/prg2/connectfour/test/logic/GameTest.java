package prg2.connectfour.test.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class GameTest {
    private final int GRID_HEIGHT = 5;
    private final int GRID_WIDTH = 10;
    private Game subject;
    private Grid grid;

    @Before
    public void setup() {
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
        this.subject = new Game(grid);
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
        Assert.assertEquals(cur.getName(), "Player 1");

        subject.nextPlayer();
        cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(cur.getName(), "Player 2");

        subject.nextPlayer();
        cur = subject.getActivePlayer();

        Assert.assertNotNull(cur);
        Assert.assertEquals(cur.getName(), "Player 1");
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
