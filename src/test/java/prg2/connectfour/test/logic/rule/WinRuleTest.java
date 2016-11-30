package prg2.connectfour.test.logic.rule;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.rule.WinRule;

public class WinRuleTest {
    private static final int GRID_WIDTH = 5;
    private static final int GRID_HEIGHT = 5;
    private WinRule subject;
    private Grid grid;
    private Player[] players;

    @Before
    public void setup() {
        this.subject = new WinRule();
        this.grid = new Grid(GRID_WIDTH, GRID_HEIGHT);
        this.players = new Player[2];
        this.players[0] = new Player("Test Player 1", Color.Red);
        this.players[1] = new Player("Test Player 2", Color.Yellow);
    }

    private void prepareGrid(String... lines) {
        assert lines.length == GRID_HEIGHT;

        for (int y = GRID_HEIGHT - 1; y >= 0; --y) {
            char[] chars = lines[y].toCharArray();
            assert chars.length == GRID_WIDTH;

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

        boolean result = WinRule.hasWon(this.grid);
        Assert.assertTrue(result);
    }
}
