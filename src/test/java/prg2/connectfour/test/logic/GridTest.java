package prg2.connectfour.test.logic;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class GridTest {
    private final int GRID_HEIGHT = 5;
    private final int GRID_WIDTH = 10;
    private Grid subject;
    private Player player;

    @Before
    public void setup() {
        this.player = new Player("Test Player", Color.Red);
        this.subject = new Grid(GRID_WIDTH, GRID_HEIGHT);
    }

    @Test
    public void testInvalidPosition() {
        Cell c = this.subject.getCellAt(10, 10);
        Assert.assertNull(c);
    }

    @Test
    public void testValidPosition() {
        Cell c = this.subject.getCellAt(0, 0);
        Assert.assertNotNull(c);
    }

    @Test
    public void testCannotDropOutsideRange() {
        boolean result = subject.dropOnColumn(player, -1);
        Assert.assertFalse(result);

        result = subject.dropOnColumn(player, GRID_WIDTH + 10);
        Assert.assertFalse(result);
    }

    @Test
    public void testCannotDropInFullColumn() {
        final int COLUMN = 4;
        boolean result;

        for (int y = 0; y < GRID_HEIGHT; ++y) {
            result = subject.dropOnColumn(player, COLUMN);
            Assert.assertTrue(result);
        }

        result = subject.dropOnColumn(player, COLUMN);
        Assert.assertFalse(result);
    }
}
