package prg2.connectfour.comlayer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import prg2.connectfour.utils.Pair;
import prg2.connectfour.utils.Utils;

public class NetworkEnv {

    private static final int lowerPortRange = 50000;
    private static final int upperPortRange = 60000;

    UdpConnection udpConnection;
    String playerName;

    private HashMap<String, Pair<InetAddress, Integer>> activeConnections = new HashMap<>();

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
                        } else if (msg instanceof InvitationResponseMsg) {
                            invitationResponseReceived((InvitationResponseMsg) msg);
                        } else if (msg instanceof InvitationMsg) {
                            invitationMsgReceived((InvitationMsg) msg);
                        } else if (msg instanceof StartGameMsg) {
                            startGameReceived((StartGameMsg) msg);
                        } else if (msg instanceof MoveMsg) {
                            moveReceived((MoveMsg) msg);
                        }
                    }
                });
                this.udpConnection.init(port);
                break;
            }
        }
    }

    private void helloMsgReceived(HelloMsg msg) {
        if (!activeConnections.containsKey(msg.getToken())) {
            activeConnections.put(msg.getToken(), new Pair<>(msg.getIpAddress(), msg.getPort()));
            HelloResponseMsg response = new HelloResponseMsg();
            response.setName(playerName);
            response.setPort(udpConnection.getListenPort());

            udpConnection.sendMessage(response, msg.getIpAddress(), msg.getPort());
            onNewPlayerDetected(msg);
        }
    }

    private void helloResponseMsgReceived(HelloResponseMsg msg) {
        if (!activeConnections.containsKey(msg.getToken())) {
            activeConnections.put(msg.getToken(), new Pair<>(msg.getIpAddress(), msg.getPort()));
            onNewPlayerDetected(msg);
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
        for (MoveHandler listener : this.moveListeners) {
            listener.movePerformed(msg.getX());
        }
    }

    public void broadcastHelloMsg() {
        HelloMsg msg = new HelloMsg();
        msg.setName(playerName);
        msg.setPort(udpConnection.getListenPort());

        for (int port = lowerPortRange; port < upperPortRange; port++)
            try {
                this.udpConnection.sendMessage(msg, InetAddress.getByName("255.255.255.255"), port);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
    }

    public String sendInvitation(BasePlayer player, int x, int y) {
        checkPlayer(player);
        InvitationMsg invatation = new InvitationMsg();
        invatation.setName(playerName);
        invatation.setX(x);
        invatation.setY(y);
        invatation.setInvitationToken(Utils.generateSecureToken());

        Pair<InetAddress, Integer> info = activeConnections.get(player.getToken());
        this.udpConnection.sendMessage(invatation, info.getLeft(), info.getRight());
        return invatation.getInvitationToken();
    }

    public void sendInvitationResponse(BasePlayer player, boolean wantToPlay) {
        checkPlayer(player);
        InvitationResponseMsg msg = new InvitationResponseMsg();
        msg.setInvitationAccepted(wantToPlay);

        Pair<InetAddress, Integer> info = activeConnections.get(player.getToken());
        this.udpConnection.sendMessage(msg, info.getLeft(), info.getRight());
    }

    public void sendStartGame(BasePlayer player, int x, int y) {
        checkPlayer(player);
        StartGameMsg msg = new StartGameMsg();
        msg.setX(x);
        msg.setY(y);

        Pair<InetAddress, Integer> info = activeConnections.get(player.getToken());
        this.udpConnection.sendMessage(msg, info.getLeft(), info.getRight());
    }

    public void sendMove(BasePlayer player, int x) {
        checkPlayer(player);
        MoveMsg msg = new MoveMsg();
        msg.setX(x);

        Pair<InetAddress, Integer> info = activeConnections.get(player.getToken());
        this.udpConnection.sendMessage(msg, info.getLeft(), info.getRight());
    }

    private void checkPlayer(BasePlayer player) {
        if (!activeConnections.containsKey(player.getToken())) {
            throw new IllegalArgumentException("Invalid token passed");
        }
    }

    private void onNewPlayerDetected(HelloMsg helloMsg) {
        BasePlayer newPlayer = new BasePlayer() {
            {
                setName(helloMsg.getName());
                setToken(helloMsg.getToken());
            }
        };
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

    public void removeAllFromSearchProcess() {
        playerListeners.clear();
        invitationListeners.clear();
        invitationResponseListeners.clear();
    }

    public interface PlayerHandler {
        void newPlayerDetected(BasePlayer newPlayer);
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
