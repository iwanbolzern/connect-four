package prg2.connectfour.ui;


import java.util.ArrayList;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JOptionPane;

public class HomeScreen extends JPanel implements ActionListener {
    private ArrayList<PlayHandler> playListeners = new ArrayList<>();

    private JButton networkButton;
    private JButton singleButton;
    private JButton loadGameButton;

    private JTextField playerNameField;

    public HomeScreen() {
        this.setLayout(new GridLayout(4, 1));

        this.playerNameField = new JTextField(30);
        this.networkButton = new JButton("Multiplayer");
        this.singleButton = new JButton("Single player");
        this.loadGameButton = new JButton("Load game");

        this.networkButton.setActionCommand("NETWORK");
        this.singleButton.setActionCommand("SINGLE");
        this.loadGameButton.setActionCommand("LOAD_GAME");

        this.networkButton.addActionListener(this);
        this.singleButton.addActionListener(this);
        this.loadGameButton.addActionListener(this);

        this.add(networkButton);
        this.add(singleButton);
        this.add(loadGameButton);
        this.add(playerNameField);
    }

    private void onPlayClick(GameMode mode, String playerName) {
        for(PlayHandler listener : playListeners)
            listener.onPlayClicked(mode, playerName);
    }

    public void addPlayListener(PlayHandler listener) {
        playListeners.add(listener);
    }

    public void actionPerformed(ActionEvent e) {
        String playerName = playerNameField.getText();
        if (playerName.isEmpty()) {
            String msg = "Please type a player name before starting a new game";
            JOptionPane.showMessageDialog(null, msg, "error",
                                          JOptionPane.ERROR_MESSAGE);
            return;
        }
        this.onPlayClick(GameMode.valueOf(e.getActionCommand()), playerName);
    }

    public interface PlayHandler {
        void onPlayClicked(GameMode mode, String playerName);
    }

    public enum GameMode {
        NETWORK,
        SINGLE,
        LOAD_GAME
    }
}
