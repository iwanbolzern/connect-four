package prg2.connectfour.comlayer.tcp;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import prg2.connectfour.comlayer.LowLevelCommunicator;
import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.controlcmds.ControlCmdReceiver;
import prg2.connectfour.comlayer.controlcmds.tcp.TcpConnectedCmd;

public class TcpLowLevelCommunicator extends LowLevelCommunicator<TcpComLayerConfig> implements ControlCmdReceiver {
	private ArrayList<TcpServer> tcpServer = new ArrayList<>();
	private HashMap<Integer, TcpConnection> tcpConnections = new HashMap<>();


	@Override
	public void init(TcpComLayerConfig config) {
		this.config = config;
		if (config.getServers() != null) {
			startServer(config);
		}

		if (config.getConnections() != null) {
			initConnections(config);
		}
	}

	@Override
	public void send(int connectionId, byte[] msg) {
		if (tcpConnections.containsKey(connectionId)) {
			tcpConnections.get(connectionId).sendMessage(msg);
		} else {
			System.out.println("Connection with id " + connectionId + " not known");
		}
	}

	private void startServer(final TcpComLayerConfig config) {
		for (TcpServerConfig serverConfig : config.getServers()) {
			TcpServer server = new TcpServer();
			try {
				server.init(serverConfig);
				server.addNewConnectionReceiver(new NewConnectionEstablishedReceiver() {

					@Override
					public void newConnectionEstablished(Socket socket) {
						final TcpConnection connection = prepareConnection();
						connection.init(socket, false);
					}
				});
				server.start();
				tcpServer.add(server);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initConnections(final TcpComLayerConfig config) {
		for (Integer connectionId : config.getConnections().keySet()) {
			if(!tcpConnections.containsKey(connectionId)) {
				TcpConnection connection = prepareConnection(connectionId);
				connection.init(config.getConnections().get(connectionId));
			} else {
				throw new IllegalArgumentException("It is not possible to have two connections with same id");
			}
		}
	}

	private TcpConnection prepareConnection() {
		final int conId;
		TcpConnection connection = new TcpConnection();
		tcpConnections.put(conId = getNewConId(), connection);
		connection.setCommunicationId(conId);
		registerReceiver(connection);
		return connection;
	}
	
	private TcpConnection prepareConnection(final int conId) {
		TcpConnection connection = new TcpConnection();
		tcpConnections.put(conId, connection);
		connection.setCommunicationId(conId);
		registerReceiver(connection);
		return connection;
	}
	
	private void registerReceiver(final TcpConnection connection) {
		connection.registerMsgReceiver(new MsgReceiver() {

			@Override
			public void msgReceived(byte[] payLoad) {
				processMessage(connection.getCommunicationId(), payLoad);
			}
		});
		connection.registerControlReceiver(new ControlCmdReceiver() {
			
			@Override
			public void receiveControlCmd(ControlCmd cmd) {
				processControlMessage(cmd);				
			}
		});
	}

	private int getNewConId() {
		Integer key = 0;
		while (key++ < Integer.MAX_VALUE) {
			if (!tcpConnections.containsKey(key))
				return key;
		}
		System.out.println("Attention you've to many connections");
		return 0;
	}

	@Override
	public void receiveControlCmd(ControlCmd cmd) {
		processControlMessage(cmd);
	}
}
