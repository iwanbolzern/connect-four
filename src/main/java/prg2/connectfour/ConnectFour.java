package prg2.connectfour;

import java.awt.Dimension;

import javax.swing.*;

import prg2.connectfour.ui.HomeScreen;
import prg2.connectfour.ui.HomeScreen.*;
import prg2.connectfour.ui.PlayGround;
import prg2.connectfour.comlayer.NetworkEnv;

public class ConnectFour extends JFrame implements PlayHandler {
    private NetworkEnv networkEnv;

    private ConnectFour() {
        HomeScreen home = new HomeScreen();
        home.addPlayListener(this);
        // add(new PlayGround(10, 10));
        add(home);
    }

    public void onPlayClick(GameMode mode, String playerName) {
        String msg = playerName + " has started a " + mode.toString();
        JOptionPane.showMessageDialog(null, msg, "information",
                                      JOptionPane.INFORMATION_MESSAGE);
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
