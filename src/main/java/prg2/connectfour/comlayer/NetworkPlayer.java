package prg2.connectfour.comlayer;

import java.net.InetAddress;

import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Player;

/**
 * Is a player from the network
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class NetworkPlayer extends Player {
    private String token;
	private InetAddress inetAdress;
	private int port;
	
	/**
	 * Creates new network player
	 * @param name from the player
	 * @param color of the player
	 */
	public NetworkPlayer(String name, Color color) {
        super(name, color);
    }
	
	/**
	 * returns ip address for the player
	 * @return ip address of the player
	 */
	public InetAddress getInetAdress() {
        return inetAdress;
    }
	
	/**
	 * sets the ip address for the player
	 * @param ip address for the player
	 */
    public void setInetAdress(InetAddress inetAdress) {
        this.inetAdress = inetAdress;
    }
    
    /**
     * returns port for the player
     * @return port of the player
     */
    public int getPort() {
        return port;
    }
    
    /**
     * sets port of the player
     * @param port udp port of the player
     */
    public void setPort(int port) {
        this.port = port;
    }
    
	/**
	 * returns identification token
	 * @return identification token
	 */
	public String getToken() {
		return token;
	}
	
	/**
	 * sets identification token
	 * @param token for identification
	 */
	public void setToken(String token) {
		this.token = token;
	}	
}
