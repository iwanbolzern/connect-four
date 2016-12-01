package prg2.connectfour;

import java.awt.Dimension;

import javax.swing.*;

import prg2.connectfour.ui.HomeScreen;
import prg2.connectfour.ui.PlayGround;
import prg2.connectfour.ui.SearchPlayerScreen;
import prg2.connectfour.ui.HomeScreen.GameMode;
import prg2.connectfour.comlayer.BasePlayer;
import prg2.connectfour.comlayer.NetworkEnv;

public class ConnectFour extends JFrame
    implements HomeScreen.PlayHandler, SearchPlayerScreen.StartGameHandler {

    private NetworkEnv networkEnv;

    // Screens
    private HomeScreen homeScreen;
    private SearchPlayerScreen searchPlayerScreen;
    private PlayGround playGround;

    private ConnectFour() {
        homeScreen = new HomeScreen();
        homeScreen.addPlayListener(this);
        add(homeScreen);
    }

    private void initNetwork(String playerName) {
        this.networkEnv = new NetworkEnv();
        this.networkEnv.init(playerName);
    }

    private void initSearchPlayerScreen(String playerName) {
        this.searchPlayerScreen = new SearchPlayerScreen(this.networkEnv);
        this.searchPlayerScreen.addStartGameListener(this);
    }

    private void initNetworkPlayGround(String gameToken, BasePlayer player, int x, int y) {
        this.playGround = new PlayGround(x, y);
        this.playGround.networkInit(this.networkEnv, gameToken, player);
    }

    private void initSinglePlayGround(int x, int y) {
        this.playGround = new PlayGround(x, y);
    }

    @Override
    public void onPlayClicked(HomeScreen.GameMode mode, String playerName) {
        if(mode == GameMode.NETWORK) {
            initNetwork(playerName);
            initSearchPlayerScreen(playerName);
            remove(homeScreen);
            add(searchPlayerScreen);
        } else if(mode == GameMode.SINGLE) {
            // TODO: show message box for x,y
            initSinglePlayGround(7, 5);
            remove(homeScreen);
            add(playGround);
            revalidate();
            repaint();
        } else if(mode == GameMode.LOAD_GAME) {
            // TODO: implement load game
        } else {
            throw new IllegalArgumentException("Game mode not known");
        }
    }

    @Override
    public void startGame(String gameToken, BasePlayer player,
                          boolean isStartSend, int x, int y) {
        if(!isStartSend) {
            networkEnv.addStartGameListener(new NetworkEnv.StartGameHandler() {
                    @Override
                    public void startGame(int x, int y) {
                        initNetworkPlayGround(gameToken, player, x, y);
                        remove(searchPlayerScreen);
                        add(playGround);
                    }
                });
        }
        initNetworkPlayGround(gameToken, player, x, y);
        remove(searchPlayerScreen);
        add(playGround);
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame mainFrame = new ConnectFour();
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setTitle("Connect four - Have fun");
        mainFrame.setPreferredSize(new Dimension(400, 200));
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
