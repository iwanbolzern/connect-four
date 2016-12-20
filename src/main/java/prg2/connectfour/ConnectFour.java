package prg2.connectfour;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import prg2.connectfour.comlayer.InvitationMsg;
import prg2.connectfour.comlayer.InvitationResponseMsg;
import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.comlayer.NetworkPlayer;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.bot.GameTheory;
import prg2.connectfour.persistence.SaveFileProvider;
import prg2.connectfour.persistence.SaveManager;
import prg2.connectfour.ui.HomeScreen;
import prg2.connectfour.ui.HomeScreen.GameMode;
import prg2.connectfour.ui.PlayGround;
import prg2.connectfour.ui.SearchPlayerScreen;

public class ConnectFour extends JFrame {
    private NetworkEnv networkEnv;
    private SaveManager saveManager;

    // Screens
    private HomeScreen homeScreen;
    private SearchPlayerScreen searchPlayerScreen;
    private PlayGround playGround;
    private String name;

    private ConnectFour() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setTitle("Connect four - Have fun");
        this.setPreferredSize(new Dimension(800, 600));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent we) {
                    try {
                        saveManager.save(playGround.getGame());
                    } catch (IOException e) {

                    }
                }
            });
        this.saveManager = new SaveManager(new SaveFileProvider());
        this.initHomeScreen();
        this.add(this.homeScreen);
        this.revalidate();
    }

    private void transition(JPanel from, JPanel to) {
        this.remove(from);
        this.add(to);
        this.revalidate();
    }

    private void initHomeScreen() {
        this.homeScreen = new HomeScreen(this.saveManager.hasSave());
        this.homeScreen.addPlayListener(new HomeScreen.PlayHandler() {
                @Override
                public void onPlayClicked(HomeScreen.GameMode mode,
                                          String playerName, int x, int y) {
                    name = playerName;
                    if(mode == GameMode.NETWORK) {
                        initNetwork(playerName);
                        initSearchPlayerScreen(playerName, x, y);
                        transition(homeScreen, searchPlayerScreen);
                    } else if(mode == GameMode.SINGLE) {
                        initSinglePlayGround(x, y, null);
                        transition(homeScreen, playGround);
                    } else if(mode == GameMode.LOAD_GAME) {
                        // Add some pseudo players, for game factory. Will be
                        // overwritten later in PlayGround.
                        Game game = saveManager.load(new Player("", Color.Red),
                                                     new Player("", Color.Yellow));
                        initSinglePlayGround(game.grid.width,
                                             game.grid.height, game);
                        transition(homeScreen, playGround);
                    } else {
                        throw new IllegalArgumentException("Game mode not known");
                    }
                }
            });
    }

    private void initNetwork(String playerName) {
        this.networkEnv = new NetworkEnv();
        this.networkEnv.init(playerName);
    }

    private void initSearchPlayerScreen(String playerName, int x, int y) {
        this.searchPlayerScreen = new SearchPlayerScreen(this.networkEnv, x, y);
        this.searchPlayerScreen.addStartGameListener(new SearchPlayerScreen.StartGameHandler() {
                @Override
                public void startGame(InvitationMsg invitation, NetworkPlayer player, InvitationResponseMsg invitationResponse) {
                    String gameToken = networkEnv.sendStartGame(player, invitation.getInvitationToken());
                    initNetworkPlayGround(gameToken, player, invitation.getX(), invitation.getY(), false);
                    transition(searchPlayerScreen, playGround);
                }

                @Override
                public void startGame(InvitationMsg invitation, NetworkPlayer player) {
                    networkEnv.addStartGameListener(new NetworkEnv.StartGameHandler() {
                        @Override
                        public void startGame(String gameToken, int x, int y) {
                            initNetworkPlayGround(gameToken, player, x, y, true);
                            transition(searchPlayerScreen, playGround);
                        }
                    });
                }
            });
        this.searchPlayerScreen.init();
    }

    private void initNetworkPlayGround(String gameToken, NetworkPlayer player, int x, int y, boolean canIStart) {
        this.playGround = new PlayGround(x, y, name);
        this.playGround.networkInit(this.networkEnv, gameToken, player, canIStart);
        this.playGround.addEndGameListener(new PlayGround.EndGameHandler() {
                @Override
                public void endGame() {
                    networkEnv.dispose();
                    networkEnv = null;
                	initHomeScreen();
                    transition(playGround, homeScreen);
                }
            });
    }

    private void initSinglePlayGround(int x, int y, Game game) {
        this.playGround = new PlayGround(x, y, name);
        GameTheory bot = new GameTheory("Best of all", Color.Yellow);
        this.playGround.singleInit(game, bot);
        this.playGround.addEndGameListener(new PlayGround.EndGameHandler() {
                @Override
                public void endGame() {
                    initHomeScreen();
                    transition(playGround, homeScreen);
                }
            });
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame mainFrame = new ConnectFour();
    }
}
