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
	private List<NewPlayerDetecter> newPlayerListeners = new ArrayList<>();
	
	public void init(String playerName) {
		this.playerName = playerName;
		initUdpConnection();
	}
	
	private void initUdpConnection() {
		for(int port = lowerPortRange; port <= upperPortRange; port++) {
			if(available(port)) {
				this.udpConnection = new UdpConnection();
				this.udpConnection.registerMsgReceiver(new UdpConnection.MsgReceiver() {
					@Override
					public void msgReceived(Msg msg) {
						if(msg instanceof HelloMsg) {
							helloMsgReceived((HelloMsg)msg);
						}
					}
				});
				this.udpConnection.init(port);
			}
		}
	}
	
	private void helloMsgReceived(HelloMsg msg) {
		if(!activeConnections.containsKey(msg.getToken())) {
			activeConnections.put(msg.getToken(), new Pair(msg.getIpAddress(), msg.getPort()));
			HelloResponseMsg response = new HelloResponseMsg() {{
				setName(playerName);
				setPort(udpConnection.getListenPort());
			}};
			udpConnection.sendMessage(response, msg.getIpAddress(), msg.getPort());
			onNewPlayerDetected((HelloMsg)msg);
		}
	}
	
	private void invatationMsgReceived(InvitationMsg invitation) {
		
	}
	
	private void invitationResponseReceived(InvitationResponseMsg invitationResponse) {
		
	}
	
	public void BroadcastHelloMsg() {
		HelloMsg msg = new HelloMsg() {{
			setName(playerName);
			setPort(udpConnection.getListenPort());
		}};
		for(int port = lowerPortRange; port < upperPortRange; port++)
			try {
				this.udpConnection.sendMessage(msg, InetAddress.getByName("255.255.255.255"), port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
	}
	
	public void SendInvitation(String token) {
		if(!activeConnections.containsKey(token)) {
			throw new IllegalArgumentException("Invalid token passed");
		}
		InvitationMsg invatation = new InvitationMsg() {{
			setName(playerName);
		}};
		Pair<InetAddress, Integer> info = activeConnections.get(token);
		this.udpConnection.sendMessage(invatation, info.getLeft(), info.getRight());
	}
	
	public void addNewPlayerListener(NewPlayerDetecter listener) {
		newPlayerListeners.add(listener);
	}
	
	private void onNewPlayerDetected(HelloMsg helloMsg) {
		for(NewPlayerDetecter listener : this.newPlayerListeners) {
			listener.newPlayerDetected(helloMsg);
		}
	}
	
	/**
	 * Checks to see if a specific port is available.
	 *
	 * @param port the port to check for availability
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
	
	public interface NewPlayerDetecter {
		void newPlayerDetected(HelloMsg helloMsg);
	}
}
