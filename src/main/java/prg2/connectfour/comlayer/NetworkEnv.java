package prg2.connectfour.comlayer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prg2.connectfour.logic.Color;
import prg2.connectfour.utils.Utils;

/**
 * Provides the whole network environment and all communication channels
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class NetworkEnv {

    private static final int lowerPortRange = 50000;
    private static final int upperPortRange = 50005;

    private UdpConnection udpConnection;
    private String playerName;

    private HashMap<String, NetworkPlayer> activeConnections = new HashMap<>();
    private HashMap<String, InvitationMsg> pendingInvitations = new HashMap<>();

    private List<PlayerHandler> playerListeners = new ArrayList<>();
    private List<InvitationHandler> invitationListeners = new ArrayList<>();
    private List<InvitationResponseHandler> invitationResponseListeners = new ArrayList<>();
    private List<StartGameHandler> startGameListeners = new ArrayList<>();
    private List<MoveHandler> moveListeners = new ArrayList<>();

    /**
     * initializes the network environment
     * @param name of the current player
     */
    public void init(String playerName) {
        this.playerName = playerName;
        initUdpConnection();
    }
    
    /**
     * closes all udp listeners and removes all
     * msg listeners.
     */
    public void dispose() {
    	this.udpConnection.dispose();
    	
    	activeConnections.clear();
    	pendingInvitations.clear();
    	
    	playerListeners.clear();
    	invitationListeners.clear();
    	invitationResponseListeners.clear();
    	startGameListeners.clear();
    	moveListeners.clear();
    }

    private void initUdpConnection() {
        for (int port = lowerPortRange; port <= upperPortRange; port++) {
            if (available(port)) {
                this.udpConnection = new UdpConnection();
                this.udpConnection.registerMsgReceiver(new UdpConnection.MsgReceiver() {
                    @Override
                    public void msgReceived(Msg msg) {
                        if (msg instanceof HelloResponseMsg) {
                            helloResponseMsgReceived((HelloResponseMsg) msg);
                        } else if (msg instanceof HelloMsg) {
                            helloMsgReceived((HelloMsg) msg);
                        }
                        if (activeConnections.containsKey(msg.getToken())) {
                            msg.setPlayer(activeConnections.get(msg.getToken()));
                            if (msg instanceof InvitationResponseMsg) {
                                invitationResponseReceived((InvitationResponseMsg) msg);
                            } else if (msg instanceof InvitationMsg) {
                                invitationMsgReceived((InvitationMsg) msg);
                            } else if (msg instanceof StartGameMsg) {
                                startGameReceived((StartGameMsg) msg);
                            } else if (msg instanceof MoveMsg) {
                                moveReceived((MoveMsg) msg);
                            }
                        }
                    }
                });
                this.udpConnection.init(port);
                break;
            }
        }
    }

    private void helloMsgReceived(HelloMsg msg) {
        System.out.println("Hello Message received" + msg.getName());
        if (!activeConnections.containsKey(msg.getToken())) {
            NetworkPlayer player = new NetworkPlayer(msg.getName(), Color.Yellow);
            player.setPort(msg.getPort());
            player.setInetAdress(msg.getIpAddress());
            player.setToken(msg.getToken());
            msg.setPlayer(player);
            activeConnections.put(msg.getToken(), player);
            HelloResponseMsg response = new HelloResponseMsg();
            response.setName(playerName);
            response.setPort(udpConnection.getListenPort());
            System.out.println("Hello response sent to " + msg.getIpAddress() + ":" + msg.getPort());
            udpConnection.sendMessage(response, msg.getIpAddress(), msg.getPort());
            onNewPlayerDetected(player);
        }
    }

    private void helloResponseMsgReceived(HelloResponseMsg msg) {
        if (!activeConnections.containsKey(msg.getToken())) {
            NetworkPlayer player = new NetworkPlayer(msg.getName(), Color.Yellow);
            player.setPort(msg.getPort());
            player.setInetAdress(msg.getIpAddress());
            player.setToken(msg.getToken());
            msg.setPlayer(player);
            activeConnections.put(msg.getToken(), player);
            onNewPlayerDetected(player);
        }
    }

    private void invitationMsgReceived(InvitationMsg invitation) {
        if (!pendingInvitations.containsKey(invitation.getInvitationToken())) {
            pendingInvitations.put(invitation.getInvitationToken(), invitation);
            for (InvitationHandler listener : this.invitationListeners) {
                listener.invitationReceived(invitation);
            }
        }
    }

    private void invitationResponseReceived(InvitationResponseMsg invitationResponse) {
        if (pendingInvitations.containsKey(invitationResponse.getInvitationToken())) {
            InvitationMsg invitation = pendingInvitations.get(invitationResponse.getInvitationToken());
            for (InvitationResponseHandler listener : this.invitationResponseListeners) {
                listener.invitationResponseReceived(invitationResponse, invitation);
            }
            pendingInvitations.remove(invitationResponse.getInvitationToken());
        }
    }

    private void startGameReceived(StartGameMsg msg) {
        if (pendingInvitations.containsKey(msg.getInvitationToken())) {
            InvitationMsg invitation = pendingInvitations.get(msg.getInvitationToken());
            for (StartGameHandler listener : this.startGameListeners) {
                listener.startGame(msg.getGameToken(), invitation.getX(), invitation.getY());
            }
            pendingInvitations.remove(msg.getInvitationToken());
        }
    }

    private void moveReceived(MoveMsg msg) {
        for (int i = 0; i < this.moveListeners.size(); i++) {
            this.moveListeners.get(i).movePerformed(msg.getX());
        }
    }

    /**
     * Sends a helloMsg to all available players in the sub-net
     */
    public void broadcastHelloMsg() {
        System.out.println("Broadcast hello message");
        HelloMsg msg = new HelloMsg();
        msg.setName(playerName);
        msg.setPort(udpConnection.getListenPort());

        for (int port = lowerPortRange; port < upperPortRange; port++)
            this.udpConnection.sendBroadcast(msg, port);
    }

    /**
     * Sends a invitation to the given network player
     * @param network player where the invitation has to be send
     * @param the width of the playground
     * @param the height of the playground
     * @return invitationToken token to identified invitation response
     */
    public String sendInvitation(NetworkPlayer player, int x, int y) {
        checkPlayer(player);
        InvitationMsg invatation = new InvitationMsg();
        invatation.setX(x);
        invatation.setY(y);
        invatation.setInvitationToken(Utils.generateSecureToken());

        this.udpConnection.sendMessage(invatation, player.getInetAdress(), player.getPort());
        pendingInvitations.put(invatation.getInvitationToken(), invatation);
        return invatation.getInvitationToken();
    }

    /**
     * Sends a invitation response for a given invitation
     * @param invitation where the response has to be sent
     * @param wantToPlay true if player wants to play
     */
    public void sendInvitationResponse(InvitationMsg invitation, boolean wantToPlay) {
        checkPlayer(invitation.getPlayer());
        InvitationResponseMsg msg = new InvitationResponseMsg();
        msg.setInvitationToken(invitation.getInvitationToken());
        msg.setInvitationAccepted(wantToPlay);

        this.udpConnection.sendMessage(msg, invitation.getPlayer().getInetAdress(), invitation.getPlayer().getPort());
    }

    /**
     * Sends a start game handshake msg to start a game
     * @param player who wants to play
     * @param invitationToken where the other player has responsed with yes.
     * @return secure game token
     */
    public String sendStartGame(NetworkPlayer player, String invitationToken) {
        checkPlayer(player);
        StartGameMsg msg = new StartGameMsg();
        msg.setInvitationToken(invitationToken);
        msg.setGameToken(Utils.generateSecureToken());
        this.udpConnection.sendMessage(msg, player.getInetAdress(), player.getPort());

        return msg.getGameToken();
    }

    /**
     * Sends a move for a given game
     * @param player who should receive the move
     * @param x coordinate of the current move
     */
    public void sendMove(NetworkPlayer player, int x) {
        checkPlayer(player);
        MoveMsg msg = new MoveMsg();
        msg.setX(x);

        this.udpConnection.sendMessage(msg, player.getInetAdress(), player.getPort());
    }

    private void checkPlayer(NetworkPlayer player) {
        if (!activeConnections.containsKey(player.getToken())) {
            throw new IllegalArgumentException("Invalid token passed");
        }
    }

    private void onNewPlayerDetected(NetworkPlayer newPlayer) {
        for (PlayerHandler listener : this.playerListeners) {
            listener.newPlayerDetected(newPlayer);
        }
    }

    /**
     * Checks if a specific port is available.
     * @param port the port to check for availability
     * @return port is available
     */
    private static boolean available(int port) {
        ServerSocket ss = null;
        DatagramSocket ds = null;
        try {
            ss = new ServerSocket(port);
            ss.setReuseAddress(true);
            ds = new DatagramSocket(port);
            ds.setReuseAddress(true);
            return true;
        } catch (IOException e) {
        } finally {
            if (ds != null) {
                ds.close();
            }

            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException e) {
                    /* should not be thrown */
                }
            }
        }

        return false;
    }

    /**
     * adds listener to receive if new player is detected in network
     * @param listener new player listener
     */
    public void addNewPlayerListener(PlayerHandler listener) {
        playerListeners.add(listener);
    }

    /**
     * adds listener to receive new invitations 
     * @param listener for invitations
     */
    public void addInvitationListener(InvitationHandler listener) {
        invitationListeners.add(listener);
    }

    /**
     * adds listener to receive invitations responses
     * @param listener for invitations responses
     */
    public void addInvitationResponseListener(InvitationResponseHandler listener) {
        invitationResponseListeners.add(listener);
    }

    /**
     * adds listener to receive start game
     * @param listener for start game
     */
    public void addStartGameListener(StartGameHandler listener) {
        startGameListeners.add(listener);
    }

    /**
     * adds listener to receive move
     * @param listener for moves
     */
    public void addMoveListener(MoveHandler listener) {
        moveListeners.add(listener);
    }

    /**
     * removes all move listeners
     */
    public void removeAllMoveListeners() {
        moveListeners.clear();
    }

    /**
     * removes all player listeners, invitation listeners and invitation response listeners
     */
    public void removeAllFromSearchProcess() {
        playerListeners.clear();
        invitationListeners.clear();
        invitationResponseListeners.clear();
    }

    /**
     * Handler for new player
     * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
     */
    public interface PlayerHandler {
        /**
         * new player detected in network
         * @param newPlayer from network
         */
        void newPlayerDetected(NetworkPlayer newPlayer);
    }

    /**
     * Handler for invitations
     * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
     */
    public interface InvitationHandler {
        /**
         * invitation received
         * @param invitation
         */
        void invitationReceived(InvitationMsg msg);
    }

    /**
     * Handler for invitations responses
     * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
     */
    public interface InvitationResponseHandler {
        /**
         * invitation response received
         * @param invitation response
         * @param corresponding invitations
         */
        void invitationResponseReceived(InvitationResponseMsg msg, InvitationMsg invitation);
    }

    /**
     * Handler for moves
     * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
     */
    public interface MoveHandler {
        /**
         * move performed
         * @param x coordinate of the move
         */
        void movePerformed(int x);
    }

    /**
     * Handler for start game
     * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
     */
    public interface StartGameHandler {
        /**
         * start game received
         * @param gameToken secure game token
         * @param x width of the play ground
         * @param y height of the play ground
         */
        void startGame(String gameToken, int x, int y);
    }
}
