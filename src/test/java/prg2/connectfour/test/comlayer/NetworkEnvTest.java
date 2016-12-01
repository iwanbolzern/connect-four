package prg2.connectfour.test.comlayer;

import static org.junit.Assert.*;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import prg2.connectfour.comlayer.BasePlayer;
import prg2.connectfour.comlayer.NetworkEnv;

public class NetworkEnvTest {
    private NetworkEnv clientOne;
    private NetworkEnv clientTwo;
    private BasePlayer playerOne;
    private BasePlayer playerTwo;
    
    private Object lock;
    
    @Before
    public void setup() {
        this.clientOne = new NetworkEnv();
        this.clientTwo = new NetworkEnv();
        
        this.clientOne.init("Iwan");
        this.clientTwo.init("Marius");
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void searchPlayerTest() {
        lock = new Object();
        
        this.clientOne.addNewPlayerListener(new NetworkEnv.PlayerHandler() {
            @Override
            public void newPlayerDetected(BasePlayer newPlayer) {
                playerTwo = newPlayer;
            }
        });
        this.clientOne.broadcastHelloMsg();
        
        this.clientTwo.addNewPlayerListener(new NetworkEnv.PlayerHandler() {
            @Override
            public void newPlayerDetected(BasePlayer newPlayer) {
                playerOne = newPlayer;
            }
        });
        this.clientTwo.broadcastHelloMsg();
        
        try {
            synchronized (lock) {
                lock.wait(10000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        Assert.assertNotNull(playerOne);
        Assert.assertNotNull(playerTwo);
        
        Assert.assertEquals(playerOne.getName(), "Iwan");
        Assert.assertEquals(playerTwo.getName(), "Marius");
    }
}