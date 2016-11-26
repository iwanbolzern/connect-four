package prg2.connectfour.comlayer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

import prg2.connectfour.comlayer.coretypes.Int32;
import prg2.connectfour.comlayer.coretypes.UInt32;
import prg2.connectfour.utils.Utils;

public class UdpConnection {
	private static final int lengthSize = 4;
	private static final long maxMsgSize = 2147483648L - 1; // 2 ^ 31 - 1

	private DatagramSocket socket;
	private int listenPort;
	private String token;

	private ArrayList<MsgReceiver> receivers = new ArrayList<>();

	private boolean isListening;
	private Thread listenThread;
	private Object lock = new Object();

	public void init(int listenPort) {
		this.listenPort = listenPort;
		this.token = Utils.generateSecureToken();
		try {
			socket = new DatagramSocket(this.listenPort);
			startListen();
		} catch (SocketException e) {
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
					
					try {
						Object msg = Utils.getObjectFromByteArray(payLoad);
						if(Msg.class.isAssignableFrom(msg.getClass())) {
							Msg message = (Msg)msg;
							message.setIpAddress(packet.getAddress());
							synchronized (lock) {
								for (MsgReceiver receiver : receivers) {
									receiver.msgReceived((Msg)msg);
								}
							}
						}
					} catch (ClassNotFoundException e) {
						System.err.println("Corrupt Msg received");
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			isListening = false;
		}
	}
	
	public int getListenPort() {
		return this.listenPort;
	}

	/**
	 * Send a message over the udp socket.
	 * 
	 * @param msg message to send
	 * @param destination destination address
	 * @param port destination port
	 */
	public void sendMessage(Msg msg, InetAddress destination, int port) {
		msg.setToken(this.token);
		byte[] byteMsg = Utils.getByteArrayFromObject(msg);
		int length = byteMsg.length;
		byte[] wholeMsg = Utils.joinArray(Int32.parse(length).getBytes(), byteMsg);
		DatagramPacket packet = new DatagramPacket(wholeMsg, wholeMsg.length, new InetSocketAddress(destination, port));
		try {
			DatagramSocket dSocket = new DatagramSocket();
			dSocket.send(packet);
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public interface MsgReceiver {
		public void msgReceived(Msg msg);

	}
}
