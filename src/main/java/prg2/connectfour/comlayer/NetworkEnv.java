package prg2.connectfour.comlayer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prg2.connectfour.logic.Color;
import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Utils;

public class NetworkEnv {

    private static final int lowerPortRange = 50000;
    private static final int upperPortRange = 50005;

    UdpConnection udpConnection;
    String playerName;

    private HashMap<String, NetworkPlayer> activeConnections = new HashMap<>();

    private List<PlayerHandler> playerListeners = new ArrayList<>();
    private List<InvitationHandler> invitationListeners = new ArrayList<>();
    private List<InvitationResponseHandler> invitationResponseListeners = new ArrayList<>();
    private List<StartGameHandler> startGameListeners = new ArrayList<>();
    private List<MoveHandler> moveListeners = new ArrayList<>();

    public void init(String playerName) {
        this.playerName = playerName;
        initUdpConnection();
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
        for (InvitationHandler listener : this.invitationListeners) {
            listener.invitationReceived(invitation);
        }
    }

    private void invitationResponseReceived(InvitationResponseMsg invitationResponse) {
        for (InvitationResponseHandler listener : this.invitationResponseListeners) {
            listener.invitationResponseReceived(invitationResponse);
        }
    }

    private void startGameReceived(StartGameMsg msg) {
        for (StartGameHandler listener : this.startGameListeners) {
            listener.startGame(msg.getX(), msg.getY());
        }
    }

    private void moveReceived(MoveMsg msg) {
        for(int i = 0; i < this.moveListeners.size(); i++) {
            this.moveListeners.get(i).movePerformed(msg.getX());
        }
    }

    public void broadcastHelloMsg() {
        System.out.println("Broadcast hello message");
        HelloMsg msg = new HelloMsg();
        msg.setName(playerName);
        msg.setPort(udpConnection.getListenPort());

        for (int port = lowerPortRange; port < upperPortRange; port++)
            this.udpConnection.sendBroadcast(msg, port);
    }

    public String sendInvitation(NetworkPlayer player, int x, int y) {
        checkPlayer(player);
        InvitationMsg invatation = new InvitationMsg();
        invatation.setName(playerName);
        invatation.setX(x);
        invatation.setY(y);
        invatation.setInvitationToken(Utils.generateSecureToken());

        this.udpConnection.sendMessage(invatation, player.getInetAdress(), player.getPort());
        return invatation.getInvitationToken();
    }

    public void sendInvitationResponse(InvitationMsg invitation, boolean wantToPlay) {
        checkPlayer(invitation.getPlayer());
        InvitationResponseMsg msg = new InvitationResponseMsg();
        msg.setX(invitation.getX());
        msg.setY(invitation.getY());
        msg.setInvitationToken(invitation.getInvitationToken());
        msg.setInvitationAccepted(wantToPlay);

        this.udpConnection.sendMessage(msg, invitation.getPlayer().getInetAdress(), invitation.getPlayer().getPort());
    }

    public void sendStartGame(NetworkPlayer player, int x, int y) {
        checkPlayer(player);
        StartGameMsg msg = new StartGameMsg();
        msg.setX(x);
        msg.setY(y);

        this.udpConnection.sendMessage(msg, player.getInetAdress(), player.getPort());
    }

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
     * Checks to see if a specific port is available.
     *
     * @param port
     *            the port to check for availability
     *
     * @return port is available
     */
    public static boolean available(int port) {
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

    public void addNewPlayerListener(PlayerHandler listener) {
        playerListeners.add(listener);
    }

    public void addInvitationListener(InvitationHandler listener) {
        invitationListeners.add(listener);
    }

    public void addInvitationResponseListener(InvitationResponseHandler listener) {
        invitationResponseListeners.add(listener);
    }

    public void addStartGameListener(StartGameHandler listener) {
        startGameListeners.add(listener);
    }

    public void addMoveListener(MoveHandler listener) {
        moveListeners.add(listener);
    }
    
    public void removeAllMoveListeners() {
        moveListeners.clear();
    }

    public void removeAllFromSearchProcess() {
        playerListeners.clear();
        invitationListeners.clear();
        invitationResponseListeners.clear();
    }

    public interface PlayerHandler {
        void newPlayerDetected(NetworkPlayer newPlayer);
    }

    public interface InvitationHandler {
        void invitationReceived(InvitationMsg msg);
    }

    public interface InvitationResponseHandler {
        void invitationResponseReceived(InvitationResponseMsg msg);
    }

    public interface MoveHandler {
        void movePerformed(int x);
    }

    public interface StartGameHandler {
        void startGame(int x, int y);
    }
}
