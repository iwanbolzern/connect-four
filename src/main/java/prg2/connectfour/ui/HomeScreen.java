package prg2.connectfour.ui;


import java.util.ArrayList;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class HomeScreen extends JPanel implements ActionListener {
    private ArrayList<PlayHandler> playListeners = new ArrayList<>();
    private Font font;

    private JButton networkButton;
    private JButton singleButton;
    private JButton loadGameButton;
    private JPanel buttonPanel;

    private JTextField playerNameField;
    private JLabel playerNameLabel;
    private JTextField columnsField;
    private JLabel columnsLabel;
    private JTextField rowsField;
    private JLabel rowsLabel;
    private JPanel textFieldsPanel;

    private String playerNameError =
        "Please type a player name before starting a multiplayer game.";
    private String rowOrColumnError =
        "Please type a column count and a row count larger than 3.";
    private JLabel errorMessage;

    public HomeScreen(boolean hasSave) {
        this.setLayout(new BorderLayout(10, 10));
        this.font = new Font("Arial", Font.PLAIN, 36);

        this.playerNameLabel = new JLabel("Player name: ");
        this.playerNameField = new JTextField(30);
        this.columnsLabel = new JLabel("Columns: ");
        this.columnsField = new JTextField(3);
        this.rowsLabel = new JLabel("Rows: ");
        this.rowsField = new JTextField(3);

        this.playerNameLabel.setFont(font);
        this.playerNameField.setFont(font);
        this.columnsLabel.setFont(font);
        this.columnsField.setFont(font);
        this.rowsLabel.setFont(font);
        this.rowsField.setFont(font);

        this.playerNameField.setText("Awesome dude");
        this.columnsField.setText("7");
        this.rowsField.setText("6");

        this.textFieldsPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        this.textFieldsPanel.setSize(new Dimension(1000, 200));

        this.textFieldsPanel.add(this.playerNameLabel);
        this.textFieldsPanel.add(this.playerNameField);
        this.textFieldsPanel.add(this.columnsLabel);
        this.textFieldsPanel.add(this.columnsField);
        this.textFieldsPanel.add(this.rowsLabel);
        this.textFieldsPanel.add(this.rowsField);

        this.networkButton = new JButton("Multiplayer");
        this.singleButton = new JButton("Single player");
        this.loadGameButton = new JButton("Load game");
        this.loadGameButton.setEnabled(hasSave);

        this.networkButton.setFont(font);
        this.singleButton.setFont(font);
        this.loadGameButton.setFont(font);

        this.networkButton.setActionCommand("NETWORK");
        this.singleButton.setActionCommand("SINGLE");
        this.loadGameButton.setActionCommand("LOAD_GAME");

        this.networkButton.addActionListener(this);
        this.singleButton.addActionListener(this);
        this.loadGameButton.addActionListener(this);

        this.buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));

        this.buttonPanel.add(networkButton);
        this.buttonPanel.add(singleButton);
        this.buttonPanel.add(loadGameButton);

        this.errorMessage = new JLabel();
        this.errorMessage.setFont(font);
        this.errorMessage.setForeground(Color.red);

        this.add(this.textFieldsPanel, BorderLayout.NORTH);
        this.add(this.buttonPanel, BorderLayout.CENTER);
        this.add(this.errorMessage, BorderLayout.SOUTH);
    }

    private void onPlayClick(GameMode mode, String playerName, int x, int y) {
        for(PlayHandler listener : playListeners)
            listener.onPlayClicked(mode, playerName, x, y);
    }

    public void addPlayListener(PlayHandler listener) {
        playListeners.add(listener);
    }

    public void actionPerformed(ActionEvent e) {
        GameMode mode = GameMode.valueOf(e.getActionCommand());

        String playerName = playerNameField.getText();
        String columns = columnsField.getText();
        String rows = rowsField.getText();

        if (playerName.isEmpty()) {
            this.errorMessage.setText(this.playerNameError);
            return;
        }

        int x, y = 0;
        try {
            x = Integer.parseInt(columns);
            y = Integer.parseInt(rows);
            if (x < 4 || y < 4) throw new Exception();
        } catch (Exception ex) {
            this.errorMessage.setText(this.rowOrColumnError);
            return;
        }

        this.onPlayClick(mode, playerName, x, y);
    }

    public interface PlayHandler {
        void onPlayClicked(GameMode mode, String playerName,
                           int x, int y);
    }

    public enum GameMode {
        NETWORK,
        SINGLE,
        LOAD_GAME
    }
}
