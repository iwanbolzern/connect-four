package prg2.connectfour.comlayer.tcp;

public class TcpServerConfig {
	
	private String name;
	private int port;

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
