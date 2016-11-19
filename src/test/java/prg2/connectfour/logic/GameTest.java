package prg2.connectfour.test.logic;

import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import prg2.connectfour.logic.Game;

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
}
