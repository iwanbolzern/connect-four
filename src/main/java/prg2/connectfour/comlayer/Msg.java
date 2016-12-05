package prg2.connectfour.comlayer;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * Base class for every message which is sent with comlayer package
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class Msg implements Serializable {
	private transient InetAddress ipAddress;
	private String token;
	private transient NetworkPlayer player;
	
	public NetworkPlayer getPlayer() {
        return player;
    }
    public void setPlayer(NetworkPlayer player) {
        this.player = player;
    }
    public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public InetAddress getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}
}
