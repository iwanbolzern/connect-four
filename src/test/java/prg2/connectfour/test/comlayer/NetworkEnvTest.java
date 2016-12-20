package prg2.connectfour.test.comlayer;

import java.util.concurrent.Semaphore;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.comlayer.NetworkPlayer;

public class NetworkEnvTest {
    private NetworkEnv clientOne;
    private NetworkEnv clientTwo;
    private NetworkPlayer playerOne;
    private NetworkPlayer playerTwo;

    @Before
    public void setup() {
        this.clientOne = new NetworkEnv();
        this.clientTwo = new NetworkEnv();

        this.clientOne.init("Iwan");
        this.clientTwo.init("Marius");
    }

    @Test
    @Ignore
    public void searchPlayerTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(2);

        this.clientOne.addNewPlayerListener(new NetworkEnv.PlayerHandler() {
            @Override
            public void newPlayerDetected(NetworkPlayer newPlayer) {
                playerTwo = newPlayer;
                semaphore.release();
            }
        });

        this.clientTwo.addNewPlayerListener(new NetworkEnv.PlayerHandler() {
            @Override
            public void newPlayerDetected(NetworkPlayer newPlayer) {
                playerOne = newPlayer;
                semaphore.release();
            }
        });

        semaphore.acquire();
        semaphore.acquire();

        this.clientOne.broadcastHelloMsg();
        this.clientTwo.broadcastHelloMsg();

        semaphore.acquire();

        Assert.assertNotNull(playerOne);
        Assert.assertNotNull(playerTwo);

        Assert.assertEquals(playerOne.name, "Iwan");
        Assert.assertEquals(playerTwo.name, "Marius");
    }
}