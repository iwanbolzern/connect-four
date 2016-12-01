package prg2.connectfour.comlayer;

import java.io.Serializable;

public class HelloMsg extends Msg implements Serializable  {
	
	private static final long serialVersionUID = 5626589539568291879L;
	
	private String name;
	private int port;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
}
