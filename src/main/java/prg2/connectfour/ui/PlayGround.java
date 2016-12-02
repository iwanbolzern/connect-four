package prg2.connectfour.ui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import prg2.connectfour.comlayer.NetworkEnv.MoveHandler;
import prg2.connectfour.comlayer.NetworkPlayer;
import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.logic.Cell;
import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Game;
import prg2.connectfour.logic.GameFactory;
import prg2.connectfour.logic.Grid;
import prg2.connectfour.logic.Player;

public class PlayGround extends JPanel implements MoveHandler {
    private Grid grid;
    private Game game;
    private Player[] players = new Player[2];
    
    private JLabel activePlayerLabel;
    private JPanel buttonPanel;
    private JPanel gridPanel;
    private JLabel slots[][];
    private JButton buttons[];

    private NetworkEnv networkEnv;

    public PlayGround(int x, int y, String myName) {               
        this.players[0] = new Player(myName, Color.Red);
        this.grid = new Grid(x, y);
        
        initComponents();
    }

    private void initGame() {
        this.game = GameFactory
                .create()
                .withGrid(this.grid)
                .withPlayers(this.players)
                .finish();
    }
    
    public void networkInit(NetworkEnv env, String gameToken, NetworkPlayer player) {
        this.networkEnv = env;
        this.players[1] = player;
        this.networkEnv.addMoveListener(new NetworkEnv.MoveHandler() {
            @Override
            public void movePerformed(int x) {
                
            }
        });
    }
    
    private void processNext() {
        if(!this.game.isFinished()) {
            Player activePlayer = this.game.getActivePlayer();
            if(activePlayer instanceof NetworkPlayer) {
                disableButtons();
                this.networkEnv.addMoveListener(this);
            } 
//            else if(activePlayer instanceof BotPlayer) {
//                TODO: insert bot mode
//            } 
            else if(activePlayer instanceof Player) {
                enableButtons();
            }
        }
    }
    
    private void enableButtons() {
        for(int x = 0; x < this.grid.getWidth(); x++) {
            Cell cell = this.grid.getCellAt(x, this.grid.getHeight());
            if(cell.getOwner() == null) {
                this.buttons[x].enableInputMethods(true);
            }
        }
    }
    
    private void disableButtons() {
        for(JButton button : this.buttons) {
            button.enableInputMethods(false);
        }
    }
    
    private void initComponents() {
        this.setLayout(new GridLayout(3, 1));
        
        this.activePlayerLabel = new JLabel();
        this.add(this.activePlayerLabel);
        
        drawButtons();
        this.add(this.buttonPanel);
        
        drawGrid();
        this.add(this.gridPanel);
    }

    private void drawButtons() {
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridLayout(1, this.grid.getWidth()));
        
        this.buttons = new JButton[this.grid.getWidth()];

        for (int i = 0; i < this.grid.getWidth(); i++) {
            buttons[i] = new JButton("" + (i + 1));
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(
                    new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            int a = Integer.parseInt(e.getActionCommand());
                        }
                    });
            this.buttonPanel.add(buttons[i]);
        }
    }
    
	private void drawGrid() {
	    if(this.gridPanel != null)
	        this.gridPanel.removeAll();
	    
	    this.gridPanel = new JPanel();
		this.gridPanel.setLayout(new GridLayout(this.grid.getHeight(), this.grid.getWidth()));

        this.slots = new JLabel[this.grid.getWidth()][this.grid.getHeight()];
        
        for (int column = 0; column < this.grid.getHeight(); column++) {
            for (int row = 0; row < this.grid.getWidth(); row++) {
                slots[row][column] = new JLabel();
                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
                slots[row][column].setBorder(new LineBorder(java.awt.Color.black));
                Cell cell = this.grid.getCellAt(column, row);
                if(cell.getOwner() != null) {
                    if(cell.getOwner().getColor() == Color.Red)
                        slots[row][column].setBackground(java.awt.Color.RED);
                    else if(cell.getOwner().getColor() == Color.Yellow)
                        slots[row][column].setBackground(java.awt.Color.YELLOW);
                    else
                        throw new IllegalArgumentException("Player Color not known");
                }
                
                this.gridPanel.add(slots[row][column]);
            }
        }
	}

    @Override
    public void movePerformed(int x) {
        this.networkEnv.removeAllMoveListeners();
        this.game.dropOnColumn(x);
        this.processNext();
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
