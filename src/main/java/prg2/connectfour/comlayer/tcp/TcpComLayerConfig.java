package prg2.connectfour.comlayer.tcp;

import java.util.ArrayList;
import java.util.HashMap;

import prg2.connectfour.comlayer.ComLayerConfig;

public class TcpComLayerConfig extends ComLayerConfig {
	private ArrayList<TcpServerConfig> servers = new ArrayList<>();
	private HashMap<Integer, TcpConnectionConfig> connections = new HashMap<>();
	
	public HashMap<Integer, TcpConnectionConfig> getConnections() {
		return connections;
	}

	public int addConnection(TcpConnectionConfig connection) {
		Integer connectionId = 1;
		while(connections.containsKey(connectionId))
			connectionId++;
		
		this.connections.put(connectionId, connection);
		return connectionId;
	}

	public ArrayList<TcpServerConfig> getServers() {
		return this.servers;
	}
	
	public void addServer(TcpServerConfig server) {
		this.servers.add(server);
	}
}
