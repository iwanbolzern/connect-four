package prg2.connectfour.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import javax.swing.DefaultListModel;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import prg2.connectfour.comlayer.NetworkPlayer;
import prg2.connectfour.comlayer.InvitationMsg;
import prg2.connectfour.comlayer.InvitationResponseMsg;
import prg2.connectfour.comlayer.NetworkEnv;
import prg2.connectfour.comlayer.NetworkEnv.*;
import prg2.connectfour.utils.Pair;

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
    private HashMap<String, NetworkPlayer> invitationTokens = new HashMap<>();

    private ArrayList<StartGameHandler> startGameListeners = new ArrayList<>();

    private JList<String> playerList;
    private ListSelectionModel listSelectionModel;
    private JScrollPane playerPane;
    private JButton inviteButton;
    private DefaultListModel playerListModel;

    /**
     * The constructor initializes the UI and registers event listeners
     * for the network interface. After initialization the constructor
     * starts a search for possible opponents.
     */
    public SearchPlayerScreen(NetworkEnv env) {
        this.networkEnv = env;
    }
    
    public void init() {
        this.setLayout(new GridLayout(1, 3));
        this.playerListModel = new DefaultListModel();
        this.playerList = new JList(playerListModel);
        this.playerList.setLayoutOrientation(JList.VERTICAL);
        this.playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.playerList.setCellRenderer(new PlayerListCellRender());

        listSelectionModel = this.playerList.getSelectionModel();
        listSelectionModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                add(inviteButton);
                revalidate();
            }
        });
        
        this.playerPane = new JScrollPane(this.playerList);
        this.add(this.playerPane);
        
        this.inviteButton = new JButton("Invite Selected");
        this.inviteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               if(e.getActionCommand() == "Invite Selected" &&
                       listSelectionModel.getMaxSelectionIndex() != -1) {
                   Pair<Integer, Integer> size = PlayGroundSizeDialog.showDialog();
                   if(size != null) {
                       NetworkPlayer player = (NetworkPlayer)playerListModel.getElementAt(listSelectionModel.getMaxSelectionIndex());
                       String newToken = networkEnv.sendInvitation(player, size.left, size.right);
                       invitationTokens.put(newToken, player);
                   }
               }
            }
        });
        revalidate();

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
     */
    @Override
    public void invitationReceived(InvitationMsg msg) {
        String str = "Do you want to accept an invitation from " +
            msg.getName() + " to play connect four?";
        int result = JOptionPane.showConfirmDialog(null, str, "Invitation",
                                                   JOptionPane.YES_NO_OPTION,
                                                   JOptionPane.QUESTION_MESSAGE);
        if(result == 0)
            this.onStartGame(msg.getInvitationToken(), msg.getPlayer(), false);
        this.networkEnv.sendInvitationResponse(msg.getPlayer(), 
                msg.getInvitationToken(), 
                result == 0 ? true : false);
    }

    /**
     * Event handler for the invitationResponseReceived event. It starts
     * a new game.
     */
    @Override
    public void invitationResponseReceived(InvitationResponseMsg msg) {
        if(this.invitationTokens.containsKey(msg.getInvitationToken())) {
            NetworkPlayer opponent = this.invitationTokens.get(msg.getInvitationToken());
            this.onStartGame(msg.getInvitationToken(), opponent, true);
        }
    }

    private void startSearch() {
        this.networkEnv.broadcastHelloMsg();
    }

    private void onStartGame(String gameToken, NetworkPlayer player, boolean hasToSendStart) {
        for(StartGameHandler listener : this.startGameListeners)
            listener.startGame(gameToken, player, hasToSendStart, 7, 5);
    }

    public void addStartGameListener(StartGameHandler listener) {
        startGameListeners.add(listener);
    }

    public interface StartGameHandler {
        void startGame(String gameToken, NetworkPlayer player, boolean hasToSendStart, int x, int y);
    }
    
    class PlayerListCellRender extends JPanel implements ListCellRenderer {
        
        public PlayerListCellRender() {
            this.setLayout(new GridBagLayout());
        }

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            GridBagConstraints c = new GridBagConstraints();
            NetworkPlayer player = (NetworkPlayer)list.getModel().getElementAt(index);
            
            JLabel nameLabel = new JLabel(player.getName());
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
