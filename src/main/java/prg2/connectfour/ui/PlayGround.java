package prg2.connectfour.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import prg2.connectfour.comlayer.BasePlayer;
import prg2.connectfour.comlayer.NetworkEnv;

import prg2.connectfour.logic.Player;
import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.Game.*;

public class PlayGround extends JPanel
    implements ActionListener, DrawListener {

    private JLabel slots[][];
    private JButton buttons[];

    private NetworkEnv networkEnv;
    private Game game;

    public PlayGround(int x, int y) {
        this.setLayout(new GridLayout(y + 1, x));

        this.slots = new JLabel[x][y];
        this.buttons = new JButton[x];

        for (int i = 0; i < x; i++) {
            buttons[i] = new JButton("" + (i + 1));
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(this);
            this.add(buttons[i]);
        }

        for (int column = 0; column < y; column++) {
            for (int row = 0; row < x; row++) {
                slots[row][column] = new JLabel();
                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
                slots[row][column].setBorder(new LineBorder(Color.black));
                slots[row][column].setOpaque(true);
                this.add(slots[row][column]);
            }
        }

        this.game = new Game(x, y);
        this.game.addDrawEventListener(this);
    }

    public void networkInit(NetworkEnv env, String gameToken, BasePlayer player) {
        this.networkEnv = env;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int x = Integer.parseInt(e.getActionCommand());
        this.game.dropOnColumn(Player.RED, x);
    }

    @Override
    public void onDraw(Player player, int x, int y) {
        this.slots[x][y].setBackground(player == Player.RED ? Color.red : Color.yellow);
    }

    public void showEndGame(GameState state) {
        String msg;
        switch (state) {
        case RED_WON:
            msg = "You won!";
            break;
        case YELLOW_WON:
            msg = "You lost!";
            break;
        case DRAW:
            msg = "Draw.";
            break;
        default:
            return;
        }
        int result = JOptionPane.showConfirmDialog(null, msg, "new game?",
                                                   JOptionPane.YES_NO_OPTION);
        if (result < 1) {
            // New game
        } else {
            // Quit
        }
    }
}
