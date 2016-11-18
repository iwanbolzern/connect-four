package prg2.connectfour.comlayer.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.ArrayList;

import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.controlcmds.ControlCmdReceiver;
import prg2.connectfour.comlayer.controlcmds.tcp.TcpConnectedCmd;
import prg2.connectfour.comlayer.controlcmds.tcp.TcpDisconnectedCmd;
import prg2.connectfour.comlayer.coretypes.UInt32;

public class TcpConnection {
	private static final int lengthSize = 4;
	private static final long maxMsgSize = 2147483648L - 1; // 2 ^ 31 - 1

	private int communicationId;

	private ArrayList<MsgReceiver> receivers = new ArrayList<>();
	private ArrayList<ControlCmdReceiver> controlReceivers = new ArrayList<>();
	private Socket socket;
	private boolean isListening;
	private Thread listenThread;
	private Thread connectThread;
	private Object lock = new Object();

	private boolean autoReconnect = true;

	private InputStream in;
	private OutputStream out;
	private ArrayList<byte[]> pendingOut = new ArrayList<>();

	public void init(final TcpConnectionConfig config) {
		init(config.getHostName(), config.getPortNumber());
	}

	public void init(Socket socket, boolean autoReconnect) {
		this.socket = socket;
		this.autoReconnect = autoReconnect;
		if (connectThread == null || !connectThread.isAlive()) {
			connectThread = new Thread(new Runnable() {
				@Override
				public void run() {
					initSocket();
				}
			});
			connectThread.start();
		}
	}

	private void init(final String hostName, final int port) {
		if (connectThread == null || !connectThread.isAlive()) {
			connectThread = new Thread(new Runnable() {
				@Override
				public void run() {
					createSocket(hostName, port);
				}
			});
			connectThread.start();
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
				byte[] msgLength = new byte[lengthSize];
				in.read(msgLength);
				long length = UInt32.parse(msgLength).getLong();

				if (length > maxMsgSize)
					System.out.println("To Long msg received");
				else {
					byte[] payLoad = new byte[(int) length];
					int bytesRead = 0;
					while (bytesRead < (int) length) {
						bytesRead += in.read(payLoad, bytesRead, (int) length - bytesRead);
					}					

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
				sendControlCmd(new TcpDisconnectedCmd() {
					{
						setCommunicationId(communicationId);
					}
				});
			}
			if (autoReconnect)
				init(socket.getInetAddress().getCanonicalHostName(), socket.getPort());
		}
	}

	/**
	 * Connect to the socket
	 * 
	 * @param hostName
	 *            IP address or DNS resolvable name
	 * @param portNumber
	 *            port number
	 */
	public void createSocket(final String hostName, final int portNumber) {
		int connectionAtteps = 0;
		boolean connected = false;
		do {
			try {
				this.socket = new Socket();
				this.socket.setSoTimeout(0); // TODO: change that
				this.socket.setReceiveBufferSize(650000); // TODO: change that
				this.socket.connect(new InetSocketAddress(hostName, portNumber));

				initSocket();
				connected = true;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Connection attemp: " + connectionAtteps++ + " try to reconnect in a second again");
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		} while (!connected && autoReconnect);
	}

	public void initSocket() {
		try {
			in = this.socket.getInputStream();
			out = this.socket.getOutputStream();
			synchronized (lock) {
				sendControlCmd(new TcpConnectedCmd() {
					{
						setCommunicationId(communicationId);
					}
				});
			}
			startListen();
			sendPendingMsgs();
		} catch (IOException e) {
			e.printStackTrace();
			if (autoReconnect)
				createSocket(socket.getInetAddress().toString(), socket.getPort());
		}
	}

	private void sendPendingMsgs() {
		// This thread is needed, if it goes something wronge the init thread is
		// not blocked.
		Thread sendThread = new Thread(new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while (i < pendingOut.size()) {
					sendMessage(pendingOut.get(0));
					synchronized (lock) {
						pendingOut.remove(0);
					}
				}
			}
		});
		sendThread.start();
	}

	/**
	 * Send a message over the tcp socket.
	 * 
	 * @param message
	 *            msg to send
	 */
	public void sendMessage(final byte[] msg) {
		try {
			if (out != null) {
				out.write(msg);
			} else {
				pendingOut.add(msg);
			}
		} catch (IOException e) {
			e.printStackTrace();
			sendControlCmd(new TcpDisconnectedCmd() {
				{
					setCommunicationId(communicationId);
				}
			});
			if (autoReconnect) {
				pendingOut.add(msg);
				init(socket.getInetAddress().getCanonicalHostName(), socket.getPort());
			}
		}
	}

	public void setCommunicationId(int conId) {
		this.communicationId = conId;
	}

	public int getCommunicationId() {
		return this.communicationId;
	}

}
