package prg2.connectfour.comlayer;

import java.net.InetAddress;

public class BasePlayer {
	String token;
	String name;
	InetAddress inetAdress;
	
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
