package prg2.connectfour.comlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;

import prg2.connectfour.utils.Utils;

public class UdpConnection {
    private static final int maxMsgSize = 65535;

    private DatagramSocket socket;
    private int listenPort;
    private String token;

    private ArrayList<MsgReceiver> receivers = new ArrayList<>();

    private boolean isListening;
    private Thread listenThread;
    private Object lock = new Object();

    public void init(int listenPort) {
        this.listenPort = listenPort;
        if (this.token == null)
            this.token = Utils.generateSecureToken();
        try {
            socket = new DatagramSocket(this.listenPort, InetAddress.getByName("0.0.0.0"));
            startListen();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void registerMsgReceiver(MsgReceiver receiver) {
        this.receivers.add(receiver);
    }

    private void startListen() {
        if (!isListening) {
            isListening = true;
            listenThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    listenForMsg();
                }
            });
            listenThread.start();
        }
    }

    private void listenForMsg() {
        while (isListening) {

            byte[] payLoad = new byte[maxMsgSize];
            DatagramPacket packet = new DatagramPacket(payLoad, payLoad.length);
            try {
                this.socket.setBroadcast(true);
                this.socket.receive(packet);

                try {
                    Object msg = Utils.getObjectFromByteArray(packet.getData());
                    if (Msg.class.isAssignableFrom(msg.getClass())) {
                        Msg message = (Msg) msg;
                        System.out.println(msg.toString() + "received from " + message.getIpAddress());
                        if (!this.token.equals(message.getToken())) {
                            message.setIpAddress(packet.getAddress());
                            synchronized (lock) {
                                for (MsgReceiver receiver : receivers) {
                                    receiver.msgReceived((Msg) msg);
                                }
                            }
                        }
                    }
                } catch (ClassNotFoundException e) {
                    System.err.println("Corrupt Msg received");
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    public int getListenPort() {
        return this.listenPort;
    }

    /**
     * Send a message over the udp socket.
     * 
     * @param msg
     *            message to send
     * @param destination
     *            destination address
     * @param port
     *            destination port
     */
    public void sendMessage(Msg msg, InetAddress destination, int port) {
        System.out.println(msg.toString() + "sent to to " + destination + ":" + port);
        msg.setToken(this.token);
        byte[] byteMsg = Utils.getByteArrayFromObject(msg);
        DatagramPacket packet = new DatagramPacket(byteMsg, byteMsg.length, new InetSocketAddress(destination, port));
        try {
            DatagramSocket dSocket = new DatagramSocket();
            dSocket.setBroadcast(true);
            dSocket.send(packet);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBroadcast(Msg msg, int port) {
        try {
            sendMessage(msg, InetAddress.getByName("255.255.255.255"), port);
        } catch (UnknownHostException ex) {
            ex.printStackTrace();
        }

        try {
            // Broadcast the message over all the network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();

                if (!networkInterface.isUp()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    sendMessage(msg, broadcast, port);

                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
    }

    public interface MsgReceiver {
        public void msgReceived(Msg msg);

    }
}
