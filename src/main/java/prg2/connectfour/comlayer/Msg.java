package prg2.connectfour.comlayer;

import java.io.Serializable;
import java.net.InetAddress;

public class Msg implements Serializable {
	private transient InetAddress ipAddress;
	private String token;
	private transient BasePlayer player;
	
	public BasePlayer getPlayer() {
        return player;
    }
    public void setPlayer(BasePlayer player) {
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
