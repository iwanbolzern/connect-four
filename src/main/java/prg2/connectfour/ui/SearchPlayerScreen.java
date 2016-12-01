package prg2.connectfour.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;

import prg2.connectfour.comlayer.BasePlayer;
import prg2.connectfour.comlayer.InvitationMsg;
import prg2.connectfour.comlayer.InvitationResponseMsg;
import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.comlayer.NetworkEnv.*;

/**
 * SearchPlayerScreen displays a list of players to choose
 * from. A player can be selected to send an invitation
 * request.
 *
 * @author David Craven <david@craven.ch>
 */
public class SearchPlayerScreen extends JPanel
    implements PlayerHandler, InvitationHandler, InvitationResponseHandler{

    private NetworkEnv networkEnv;
    private HashMap<String, BasePlayer> invitationTokens = new HashMap<>();

    private ArrayList<StartGameHandler> startGameListeners = new ArrayList<>();

    private JList<String> playerList;
    private JScrollPane playerPane;
    private DefaultListModel<String> playerListModel;

    /**
     * The constructor initializes the UI and registers event listeners
     * for the network interface. After initialization the constructor
     * starts a search for possible opponents.
     */
    public SearchPlayerScreen(NetworkEnv env) {
        this.setLayout(new GridLayout(1, 3));
        this.networkEnv = env;

        // init all ui components here
        this.playerListModel = new DefaultListModel<>();

        this.playerListModel.addElement("player 1");
        this.playerListModel.addElement("player 2");
        this.playerListModel.addElement("player 3");
        this.playerListModel.addElement("player 4");

        this.playerList = new JList<>(this.playerListModel);
        this.playerList.setLayoutOrientation(JList.VERTICAL);
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.add(playerList);

        this.playerPane = new JScrollPane(this.playerList);
        this.add(this.playerPane);

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
     */
    @Override
    public void newPlayerDetected(BasePlayer newPlayer) {
        this.invitationTokens.put(newPlayer.getToken(), newPlayer);
        this.playerListModel.addElement(newPlayer.getName());
    }

    /**
     * Event handler for the invitationReceived event. It provides
     * message dialog to accept or reject an invitation and sends
     * an invitation response. An invitation response is also sent
     * when rejected.
     */
    @Override
    public void invitationReceived(InvitationMsg msg) {
        String str = "Do you want to accept an invitation from " +
            msg.getName() + " to play connect four?";
        int result = JOptionPane.showConfirmDialog(null, str, "Invitation",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE);
        if (this.invitationTokens.containsKey(msg.getInvitationToken())) {
            BasePlayer opponent = this.invitationTokens.get(msg.getInvitationToken());
            this.networkEnv.sendInvitationResponse(opponent, result == 0 ? true : false);
        }
    }

    /**
     * Event handler for the invitationResponseReceived event. It starts
     * a new game.
     */
    @Override
    public void invitationResponseReceived(InvitationResponseMsg msg) {
        if(this.invitationTokens.containsKey(msg.getInvitationToken())) {
            BasePlayer opponent = this.invitationTokens.get(msg.getInvitationToken());
            this.onStartGame(msg.getInvitationToken(), opponent);
        }
    }

    private void startSearch() {
        this.networkEnv.broadcastHelloMsg();
    }

    private void invitePlayer(BasePlayer player) {
        String newToken = this.networkEnv.sendInvitation(player);
        invitationTokens.put(newToken, player);
    }

    private void onStartGame(String gameToken, BasePlayer player) {
        for(StartGameHandler listener : this.startGameListeners)
            listener.startGame(gameToken, player, true, 7, 7);
    }

    public void addStartGameListener(StartGameHandler listener) {
        startGameListeners.add(listener);
    }

    public interface StartGameHandler {
        void startGame(String gameToken, BasePlayer player, boolean isStartSend, int x, int y);
    }
}
