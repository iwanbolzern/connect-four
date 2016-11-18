package prg2.connectfour.comlayer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import prg2.connectfour.comlayer.CmdCallBack;
import prg2.connectfour.comlayer.Communicator;
import prg2.connectfour.comlayer.tcp.TcpComLayerConfig;
import prg2.connectfour.comlayer.tcp.TcpConnectionConfig;
import prg2.connectfour.comlayer.tcp.TcpServerConfig;

public class server {
	public static void main(String[] args) {
		
		TcpComLayerConfig tcpConfig = new TcpComLayerConfig() {{
			addServer(new TcpServerConfig() {{
				setPort(8080);
				setName("My First TCP Server");
			}});
			addConnection(new TcpConnectionConfig() {{
				setHostName("foreignTCPServer.com");
				setPortNumber(8020);
			}});
			setUsedCharset(StandardCharsets.US_ASCII);
		}};
		
		Communicator com = new Communicator();
		com.registerCmd(1, new CmdCallBack<ExtendedPayLoad>(ExtendedPayLoad.class) {
			@Override
			public void receive(int communicationId, ExtendedPayLoad payLoad) {
				System.out.println("Number: " + payLoad.getNumber());
				System.out.println("Label: " + payLoad.getLabel());
			}
		});
		com.init(tcpConfig);
		waitForExit();
	}
	
	public static void waitForExit() {
		try {
			Thread.sleep(1000); // Just wait a little that the following output
								// is at the bottom
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = "";
		while (true) {
			System.out.println("Write \"exit\" to exit the program");
			try {
				input = br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			input = input.toLowerCase();
			if (input.startsWith("e")) {
				break;
			} 
		}
	}
}
