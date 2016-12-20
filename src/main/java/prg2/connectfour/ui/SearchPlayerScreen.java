package prg2.connectfour.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

import prg2.connectfour.comlayer.InvitationMsg;
import prg2.connectfour.comlayer.InvitationResponseMsg;
import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.comlayer.NetworkEnv.InvitationHandler;
import prg2.connectfour.comlayer.NetworkEnv.InvitationResponseHandler;
import prg2.connectfour.comlayer.NetworkEnv.PlayerHandler;
import prg2.connectfour.comlayer.NetworkPlayer;

/**
 * SearchPlayerScreen displays a list of players to choose
 * from. A player can be selected to send an invitation
 * request.
 *
 * @author David Craven {@literal <david@craven.ch>}
 */
public class SearchPlayerScreen extends JPanel
        implements PlayerHandler, InvitationHandler, InvitationResponseHandler {

    private NetworkEnv networkEnv;
    private HashMap<String, NetworkPlayer> invitationTokens = new HashMap<>();

    private ArrayList<StartGameHandler> startGameListeners = new ArrayList<>();

    private Font font;
    private JLabel lookingForPlayers;
    private JList<NetworkPlayer> playerList;
    private ListSelectionModel listSelectionModel;
    private JScrollPane playerPane;
    private DefaultListModel<NetworkPlayer> playerListModel;

    private int x;
    private int y;

    /**
     * The constructor initializes the UI and registers event listeners
     * for the network interface. After initialization the constructor
     * starts a search for possible opponents.
     *
     * @param env Network environment
     * @param x X coordinate
     * @param y Y coordinate
     */
    public SearchPlayerScreen(NetworkEnv env, int x, int y) {
        this.networkEnv = env;
        this.x = x;
        this.y = y;
    }

    public void init() {
        this.setLayout(new BorderLayout(10, 10));
        this.font = new Font("Arial", Font.PLAIN, 36);

        this.lookingForPlayers = new JLabel("Looking for players");
        this.lookingForPlayers.setFont(this.font);
        this.add(this.lookingForPlayers, BorderLayout.NORTH);

        this.playerListModel = new DefaultListModel<>();
        this.playerList = new JList<>(playerListModel);
        this.playerList.setLayoutOrientation(JList.VERTICAL);
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.playerList.setCellRenderer(new PlayerListCellRender());
        this.playerList.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    NetworkPlayer player = playerList.getSelectedValue();
                    String newToken = networkEnv.sendInvitation(player, x, y);
                    invitationTokens.put(newToken, player);
                }
            });

        this.playerPane = new JScrollPane(this.playerList);
        this.add(this.playerPane, BorderLayout.CENTER);

        // register listeners on network env
        this.networkEnv.addNewPlayerListener(this);
        this.networkEnv.addInvitationListener(this);
        this.networkEnv.addInvitationResponseListener(this);

        startSearch();
    }

    /**
     * Event handler for the newPlayerDetected event. It adds the
     * player to the invitationTokens HashMap and inserts a new
     * player into the UI.
     *
     * @param newPlayer New player
     */
    @SuppressWarnings("unchecked")
    @Override
    public void newPlayerDetected(NetworkPlayer newPlayer) {
        this.playerListModel.addElement(newPlayer);
        revalidate();
    }

    /**
     * Event handler for the invitationReceived event. It provides
     * message dialog to accept or reject an invitation and sends
     * an invitation response. An invitation response is also sent
     * when rejected.
     *
     * @param msg Invitation message
     */
    @Override
    public void invitationReceived(InvitationMsg msg) {
        String str = "Do you want to accept an invitation from " +
                msg.getPlayer().name + " to play connect four?";
        int result = JOptionPane.showConfirmDialog(null, str, "Invitation",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
        if (result == 0) {
            this.onStartGame(msg, msg.getPlayer());
        }

        this.networkEnv.sendInvitationResponse(msg, result == 0);
    }

    /**
     * Event handler for the invitationResponseReceived event. It starts
     * a new game.
     *
     * @param msg Invitation response
     * @param invitation Invitation message
     */
    @Override
    public void invitationResponseReceived(InvitationResponseMsg msg, InvitationMsg invitation) {
        if (this.invitationTokens.containsKey(msg.getInvitationToken())) {
            NetworkPlayer opponent = this.invitationTokens.get(msg.getInvitationToken());
            this.onStartGame(invitation, opponent, msg);
        }
    }

    private void startSearch() {
        this.networkEnv.broadcastHelloMsg();
    }

    private void onStartGame(InvitationMsg invitation, NetworkPlayer player, InvitationResponseMsg invitationResponse) {
        for (StartGameHandler listener : this.startGameListeners)
            listener.startGame(invitation, player, invitationResponse);
    }

    private void onStartGame(InvitationMsg invitation, NetworkPlayer player) {
        for (StartGameHandler listener : this.startGameListeners)
            listener.startGame(invitation, player);
    }

    public void addStartGameListener(StartGameHandler listener) {
        startGameListeners.add(listener);
    }

    public interface StartGameHandler {
        void startGame(InvitationMsg invitation, NetworkPlayer player, InvitationResponseMsg invitationResponse);

        void startGame(InvitationMsg invitation, NetworkPlayer player);
    }

    private class PlayerListCellRender extends JPanel implements ListCellRenderer<NetworkPlayer> {
        PlayerListCellRender() {
            this.setLayout(new GridBagLayout());
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends NetworkPlayer> list, NetworkPlayer value, int index, boolean isSelected, boolean cellHasFocus) {
            GridBagConstraints c = new GridBagConstraints();

            JLabel nameLabel = new JLabel(value.name);
            nameLabel.setFont(font);

            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0.8;
            this.add(nameLabel, c);

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            return this;
        }
    }
}
