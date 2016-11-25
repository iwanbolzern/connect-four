package prg2.connectfour.test.logic;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.Player;

public class GameTest {
    private Game subject;

    @Before
    public void setup() {
        this.subject = new Game();
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
}
