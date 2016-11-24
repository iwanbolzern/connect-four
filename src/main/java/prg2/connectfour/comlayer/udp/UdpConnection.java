package prg2.connectfour.comlayer.udp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.controlcmds.ControlCmdReceiver;
import prg2.connectfour.comlayer.controlcmds.tcp.TcpConnectedCmd;
import prg2.connectfour.comlayer.controlcmds.tcp.TcpDisconnectedCmd;
import prg2.connectfour.comlayer.controlcmds.udp.UdpDisconnectedCmd;
import prg2.connectfour.comlayer.coretypes.UInt32;

public class UdpConnection {
	private static final int lengthSize = 4;
	private static final long maxMsgSize = 2147483648L - 1; // 2 ^ 31 - 1

	private int communicationId;

	private DatagramSocket socket;
	private int port;
	private String destination;

	private ArrayList<MsgReceiver> receivers = new ArrayList<>();
	private ArrayList<ControlCmdReceiver> controlReceivers = new ArrayList<>();

	private boolean isListening;
	private Thread listenThread;
	private Object lock = new Object();

	private ArrayList<byte[]> pendingOut = new ArrayList<>();

	public void init(final UdpConnectionConfig config) {
		this.port = config.getPortNumber();
		this.destination = config.getHostName();
		try {
			socket = new DatagramSocket(this.port);
			startListen();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public void registerMsgReceiver(MsgReceiver receiver) {
		this.receivers.add(receiver);
	}

	public void registerControlReceiver(ControlCmdReceiver receiver) {
		this.controlReceivers.add(receiver);
	}

	private void sendControlCmd(ControlCmd cmd) {
		for (ControlCmdReceiver receiver : controlReceivers)
			receiver.receiveControlCmd(cmd);
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
		try {
			while (isListening) {
				// receive length of package
				byte[] msgLength = new byte[lengthSize];
				DatagramPacket packet = new DatagramPacket(msgLength, msgLength.length);
				this.socket.receive(packet);

				long length = UInt32.parse(packet.getData()).getLong();
				if (length > maxMsgSize)
					System.out.println("To Long msg received");
				else {
					byte[] payLoad = new byte[(int) length];
					packet = new DatagramPacket(payLoad, payLoad.length);
					this.socket.receive(packet);

					synchronized (lock) {
						for (MsgReceiver receiver : receivers) {
							receiver.msgReceived(payLoad);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			isListening = false;
			synchronized (lock) {
				sendControlCmd(new UdpDisconnectedCmd() {
					{
						setCommunicationId(communicationId);
					}
				});
			}
		}
	}

	/**
	 * Send a message over the udp socket.
	 * 
	 * @param message
	 *            msg to send
	 */
	public void sendMessage(final byte[] msg) {
		DatagramPacket packet = new DatagramPacket(msg, msg.length, new InetSocketAddress(this.destination, this.port));
		try {
			DatagramSocket dSocket = new DatagramSocket();
			dSocket.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setCommunicationId(int conId) {
		this.communicationId = conId;
	}

	public int getCommunicationId() {
		return this.communicationId;
	}

}
