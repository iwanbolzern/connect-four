package prg2.connectfour.comlayer;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Base class for every message which is sent with comlayer package
 * @author Iwan Bolzern {@literal <iwan.bolzern@ihomelab.ch>}
 */
public class Msg implements Serializable {
	private transient InetAddress ipAddress;
	private String token;
	private transient NetworkPlayer player;
	
	/**
	 * Returns corresponding network player for the given msg
	 * Is just available on the receiving side of the connection
	 * (is not transmitted over the network)
	 * @return corresponding network player
	 */
	public NetworkPlayer getPlayer() {
        return player;
    }
	
	/** 
	 * Sets corresponding network player for the given msg
	 * Will be set on the receiving side of the connection. 
	 * (is not transmitted over the network)
	 * @param corresponding network player
	 */
    public void setPlayer(NetworkPlayer player) {
        this.player = player;
    }
    
    /**
     * Returns the token for the given connection
     * @return secure token for the given connection
     */
    public String getToken() {
		return token;
	}
    
    /**
     * Sets the secure connection token
     * @param secure connection token
     */
	public void setToken(String token) {
		this.token = token;
	}
	
	/**
	 * Returns the ip address of the given communication partner
	 * (is not transmitted over the network)
	 * @return returns the ip address of the communication end-point
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}
	
	/**
	 * Sets the ip address of the communication end-point
	 * (is not transmitted over the network)
	 * @param ip address of the communication end-point
	 */
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
}
