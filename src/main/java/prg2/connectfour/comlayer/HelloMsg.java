package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Message which is sent to inform other players that new player is available
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class HelloMsg extends Msg implements Serializable  {
	
	private static final long serialVersionUID = 5626589539568291879L;
	
	private String name;
	private int port;
	
	/**
	 * Returns name of player
	 * @return Players Name
	 */
	public String getName() {
		return name;
	}
	
	/** 
	 * Sets players name
	 * @param players name
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Port where UDP socket is listening
	 * @return UDP Port of listening server
	 */
	public int getPort() {
		return port;
	}
	
	/**
	 * Sets UDP port where server is listening
	 * @param listening UDP port
	 */
	public void setPort(int port) {
		this.port = port;
	}
}
