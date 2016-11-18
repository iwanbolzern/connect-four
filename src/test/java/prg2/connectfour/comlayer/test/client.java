package prg2.connectfour.comlayer.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import prg2.connectfour.comlayer.Communicator;
import prg2.connectfour.comlayer.tcp.TcpComLayerConfig;
import prg2.connectfour.comlayer.tcp.TcpConnectionConfig;

public class client {

	public static void main(String[] args) {
final ExtendedPayLoad sendPayLoad = new ExtendedPayLoad();
sendPayLoad.setLabel("Hans");
sendPayLoad.setNumber(99);

		int conId;
		TcpComLayerConfig clientConfig = new TcpComLayerConfig();
		conId = clientConfig.addConnection(new TcpConnectionConfig() {
			{
				setHostName("localhost");
				setPortNumber(8080);
			}
		});
		clientConfig.setUsedCharset(StandardCharsets.US_ASCII);

		Communicator clientCom = new Communicator();
		clientCom.init(clientConfig);
		clientCom.send(conId, 1, sendPayLoad);

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
