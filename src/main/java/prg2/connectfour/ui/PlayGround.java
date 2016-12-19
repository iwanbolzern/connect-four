package prg2.connectfour.ui;

import java.util.ArrayList;

import java.awt.Font;
import java.awt.Dimension;
import java.awt.BorderLayout;
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
import prg2.connectfour.logic.bot.GameTheory;

public class PlayGround extends JPanel implements MoveHandler {
    private ArrayList<EndGameHandler> endGameListeners = new ArrayList<>();

    private Grid grid;
    private Game game;
    private Player[] players = new Player[2];

    private JLabel activePlayerLabel;
    private JPanel buttonPanel;
    private JPanel gridPanel;
    private JLabel slots[][];
    private JButton buttons[];

    private NetworkEnv networkEnv;
    private NetworkPlayer networkPlayer;
    private String gameToken;

    private Font font;

    public PlayGround(int x, int y, String myName) {
        this.font = new Font("Arial", Font.PLAIN, 36);
        this.players[0] = new Player(myName, Color.Red);
        this.grid = new Grid(x, y);
    }

    private void initGame(Game game) {
        if (game != null) {
            this.game = game;
            this.grid = game.grid;
            this.game.setPlayers(this.players);
        } else {
            this.game = GameFactory.create().withGrid(this.grid).withPlayers(this.players).finish();
        }
        initComponents();
        revalidate();
    }

    public void networkInit(NetworkEnv env, String gameToken, NetworkPlayer player, boolean canIStart) {
        this.networkEnv = env;
        this.gameToken = gameToken;
        this.networkPlayer = player;
        this.players[1] = player;

        if (!canIStart) {
            this.players[1] = this.players[0];
            this.players[0] = player;
        }

        initGame(null);
        processNext();
    }

    public void singleInit(Game game, GameTheory bot) {
        this.players[1] = bot;

        initGame(game);
        processNext();
    }

    private void processNext() {
        drawGrid();
        if (!this.game.isFinished()) {
            Player activePlayer = this.game.getActivePlayer();
            if (activePlayer instanceof NetworkPlayer) {
                disableButtons();
                this.activePlayerLabel.setText("Opponent's turn");
                this.networkEnv.addMoveListener(this);
            } else if (activePlayer instanceof GameTheory) {
                disableButtons();
                int nextMove = ((GameTheory) activePlayer).getNextMove(this.grid);
                this.game.dropOnColumn(nextMove);
                processNext();
            } else if (activePlayer instanceof Player) {
                this.activePlayerLabel.setText("Your turn");
                enableButtons();
            }
        } else {
            revalidate();
            showFinish();
            onEndGame();
        }
        revalidate();
    }

    private void enableButtons() {
        for (int x = 0; x < this.grid.width; x++) {
            Cell cell = this.grid.getCellAt(x, this.grid.height - 1);
            if (cell.getOwner() == null) {
                this.buttons[x].setEnabled(true);
            }
        }
    }

    private void disableButtons() {
        for (JButton button : this.buttons) {
            button.setEnabled(false);
        }
    }

    private void initComponents() {
        this.setLayout(new BorderLayout(10, 10));

        this.activePlayerLabel = new JLabel();
        //this.add(this.activePlayerLabel);

        drawButtons();
        this.add(this.buttonPanel, BorderLayout.NORTH);

        drawGrid();
        this.add(this.gridPanel, BorderLayout.CENTER);
    }

    private void drawButtons() {
        this.buttonPanel = new JPanel();
        this.buttonPanel.setLayout(new GridLayout(1, this.grid.width));

        this.buttons = new JButton[this.grid.width];

        for (int i = 0; i < this.grid.width; i++) {
            buttons[i] = new JButton("" + (i + 1));
            buttons[i].setFont(this.font);
            Dimension d = buttons[i].getPreferredSize();
            d.height = 100;
            buttons[i].setPreferredSize(d);
            buttons[i].setActionCommand("" + i);
            buttons[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int a = Integer.parseInt(e.getActionCommand());
                    game.dropOnColumn(a);
                    if (networkEnv != null)
                        networkEnv.sendMove(networkPlayer, a);
                    processNext();
                }
            });
            this.buttonPanel.add(buttons[i]);
        }
    }

    private void drawGrid() {
        if (this.gridPanel != null)
            this.gridPanel.removeAll();
        else
            this.gridPanel = new JPanel();

        this.gridPanel.setLayout(new GridLayout(this.grid.height, this.grid.width));

        this.slots = new JLabel[this.grid.height][this.grid.width];

        for (int row = this.grid.height - 1; row >= 0; row--) {
            for (int column = 0; column < this.grid.width; column++) {

                slots[row][column] = new JLabel();
                slots[row][column].setHorizontalAlignment(SwingConstants.CENTER);
                slots[row][column].setBorder(new LineBorder(java.awt.Color.black));
                slots[row][column].setOpaque(true);
                Cell cell = this.grid.getCellAt(column, row);
                if (cell.getOwner() != null) {
                    if (cell.getOwner().color == Color.Red) {
                        slots[row][column].setBackground(java.awt.Color.red);
                    } else if (cell.getOwner().color == Color.Yellow) {
                        slots[row][column].setBackground(java.awt.Color.yellow);
                    } else
                        throw new IllegalArgumentException("Player color unknown");
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

    public void showFinish() {
        String msg, title;
        if (this.game.getWinner() == null) {
            msg = "No more fields.";
            title = "Draw";
        }
        else if (this.game.getWinner() instanceof NetworkPlayer) {
            msg = this.game.getWinner().name + " is much better than you. Go home and cry.";
            title = "Loser";
        } else if (this.game.getWinner() instanceof GameTheory) {
            msg = "Really, you're worse than a computer.";
            title = "Loser";
        } else {
            msg = "Well done!";
            title = "Winner";
        }
        JOptionPane.showMessageDialog(this, msg, title, JOptionPane.INFORMATION_MESSAGE);

    }

    public Game getGame() {
        return this.game;
    }

    public void addEndGameListener(EndGameHandler handler) {
        this.endGameListeners.add(handler);
    }

    private void onEndGame() {
        for (EndGameHandler listener : this.endGameListeners) {
            listener.endGame();
        }
    }

    public interface EndGameHandler {
        void endGame();
    }
}
