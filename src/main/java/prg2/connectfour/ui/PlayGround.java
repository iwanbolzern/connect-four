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
import prg2.connectfour.logic.GameFactory;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class PlayGround extends JPanel {
    private JLabel slots[][];
    private JButton buttons[];

    private NetworkEnv networkEnv;

    public PlayGround(int x, int y) {
        initComponents(x, y);
    }

    public void networkInit(NetworkEnv env, String gameToken, BasePlayer player) {
        this.networkEnv = env;
        this.networkEnv.addMoveListener(new NetworkEnv.MoveHandler() {
            @Override
            public void movePerformed(int x) {
                
            }
        });
    }

	private void initComponents(int x, int y) {
		this.setLayout(new GridLayout(x, y + 1));

        this.slots = new JLabel[x][y];
        this.buttons = new JButton[x];

        for (int i = 0; i < x; i++) {
            buttons[i] = new JButton("" + (i + 1));
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(
                    new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            int a = Integer.parseInt(e.getActionCommand());


//                            int y = my_grid.find_y(a);//check for space in collumn
//                            if (y != -1) {
//                                //sets a place to current player
//                                if (my_logic.set_and_check(a, y, currentPlayer)) {
//                                    hasWon = true;
//                                } else if (my_logic.draw_game()) {//checks for drawgame
//                                    hasDraw = true;
//                                } else {
//                                    //change player
//                                    currentPlayer = my_grid.changeplayer(currentPlayer, 2);
//                                    frame.setTitle("connect four - player " + currentPlayer + "'s turn");
//                                }
//                            } else {
//                                JOptionPane.showMessageDialog(null, "choose another one", "column is filled", JOptionPane.INFORMATION_MESSAGE);
//                            }
                        }
                    });
            this.add(buttons[i]);
        }
        for (int column = 0; column < y; column++) {
            for (int row = 0; row < x; row++) {
                slots[row][column] = new JLabel();
                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
                slots[row][column].setBorder(new LineBorder(Color.black));
                this.add(slots[row][column]);
            }
        }
	}

//	 public void showWon() {
//	        String winner = "player " + currentPlayer + " wins";
//	        int n = JOptionPane.showConfirmDialog(
//	                frame,
//	                "new game?",
//	                winner,
//	                JOptionPane.YES_NO_OPTION);
//	        if (n < 1) {
//	            frame.dispose();
//	            newGame = true;
//	        } else {
//	            frame.dispose();
//	            quit = true;
//	        }
//	    }
//
//	    public void showDraw() {
//	        String winner = "draw game";
//	        int n = JOptionPane.showConfirmDialog(
//	                frame,
//	                "new game?",
//	                winner,
//	                JOptionPane.YES_NO_OPTION);
//	        if (n < 1) {
//	            frame.dispose();
//	            newGame = true;
//	        } else {
//	            frame.dispose();
//	            quit = true;
//	        }
//	    }
}
