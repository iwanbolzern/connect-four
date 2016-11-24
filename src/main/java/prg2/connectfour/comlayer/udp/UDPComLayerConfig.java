package prg2.connectfour.comlayer.udp;

import java.util.HashMap;

import prg2.connectfour.comlayer.ComLayerConfig;

public class UDPComLayerConfig extends ComLayerConfig {
	
	private HashMap<Integer, UdpConnectionConfig> connections = new HashMap<>();
	
	public int addConnection(UdpConnectionConfig connection) {
		Integer connectionId = 1;
		while(connections.containsKey(connectionId))
			connectionId++;
		
		this.connections.put(connectionId, connection);
		return connectionId;
	}
	
	public HashMap<Integer, UdpConnectionConfig> getConnections() {
		return this.connections;
	}
}
