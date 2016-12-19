package prg2.connectfour.test.logic.rule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.rule.IteratorWinRule;

public class IteratorWinRuleTest {
    private static final int GRID_WIDTH = 5;
    private static final int GRID_HEIGHT = 5;
    private Grid grid;
    private Player[] players;

    @Before
    public void setup() {
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
        this.players = new Player[2];
        this.players[0] = new Player("Test Player 1", Color.Red);
        this.players[1] = new Player("Test Player 2", Color.Yellow);
    }

    private void prepareGrid(String... lines) {
        assert lines.length == this.grid.height;

        for (int y = this.grid.height - 1; y >= 0; --y) {
            char[] chars = lines[y].toCharArray();
            assert chars.length == this.grid.width;

            for (int x = 0; x < chars.length; x++) {
                int num = chars[x] - '0';
                if (num == 0)
                    continue;

                Player player = this.players[num - 1];
                grid.dropOnColumn(player, x);
            }
        }
    }

    @Test
    public void testDetectsHorizontalWin() {
        prepareGrid(
                "00000",
                "00000",
                "01210",
                "01111",
                "22121");

        Player result = IteratorWinRule.Horizontal.playerWon(this.grid);
        Assert.assertEquals(this.players[0], result);
    }

    @Test
    public void testDetectsVerticalWin() {
        prepareGrid(
                "00000",
                "00200",
                "01210",
                "01211",
                "21221");

        Player result = IteratorWinRule.Vertical.playerWon(this.grid);
        Assert.assertEquals(this.players[1], result);
    }

    @Test
    public void testDetectsDiagonalRightWin() {
        prepareGrid(
                "00000",
                "00002",
                "01021",
                "01211",
                "22121");

        Player result = IteratorWinRule.DiagonalRight.playerWon(this.grid);
        Assert.assertEquals(this.players[1], result);
    }

    @Test
    public void testDetectsDiagonalLeftWin() {
        prepareGrid(
                "00000",
                "20000",
                "12001",
                "11211",
                "21121");

        Player result = IteratorWinRule.DiagonalLeft.playerWon(this.grid);
        Assert.assertEquals(this.players[1], result);
    }

    /**
     * Test related to bug #9
     * https://github.com/iwanbolzern/connect-four/issues/9
     */
    @Test
    public void testBug9() {
        this.grid = new Grid(7, 6);

        prepareGrid(
                "0000000",
                "0002000",
                "0001000",
                "0001000",
                "2111220",
                "2211212"
        );

        Player result = IteratorWinRule.Vertical.playerWon(this.grid);
        Assert.assertEquals(this.players[0], result);
    }
}
