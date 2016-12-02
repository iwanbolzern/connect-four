package prg2.connectfour.comlayer;

import java.net.InetAddress;

import prg2.connectfour.logic.Color;
import prg2.connectfour.logic.Player;

public class NetworkPlayer extends Player {
    String token;
	InetAddress inetAdress;
	
	public NetworkPlayer(String name, Color color) {
        super(name, color);
    }
	
	public InetAddress getInetAdress() {
        return inetAdress;
    }
    public void setInetAdress(InetAddress inetAdress) {
        this.inetAdress = inetAdress;
    }
    public int getPort() {
        return port;
    }
    public void setPort(int port) {
        this.port = port;
    }
    int port;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}	
}
