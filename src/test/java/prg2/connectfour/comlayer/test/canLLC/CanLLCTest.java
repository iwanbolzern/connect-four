package prg2.connectfour.comlayer.test.canLLC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import prg2.connectfour.comlayer.LowLevelCmdReceiver;
import prg2.connectfour.comlayer.LowLevelCommunicator;
import prg2.connectfour.comlayer.can.CanComLayerConfig;
import prg2.connectfour.comlayer.can.CanLowLevelCommunicator;
import prg2.connectfour.comlayer.can.SerialBaudRate;
import prg2.connectfour.comlayer.can.converter.CanBitRate;
import prg2.connectfour.comlayer.can.converter.CanConverter;
import prg2.connectfour.comlayer.controlcmds.ControlCmd;

public class CanLLCTest {

	public static void main(String[] args) {
		CanComLayerConfig config = new CanComLayerConfig();
		config.setCanBitRate(CanBitRate._1000);
		config.setCanConverter(CanConverter.CanUsbW682);
		config.setSerialBaudRate(SerialBaudRate._115200);;
		config.setUsedCharset(StandardCharsets.US_ASCII);
		config.setPort("COM9");
		
		LowLevelCommunicator llc = new CanLowLevelCommunicator();
		llc.addCmdReceiver(new LowLevelCmdReceiver() {
			
			@Override
			public void receiveControlCmd(ControlCmd cmd) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void receive(int connectionId, int cmdId, byte[] payLoad) {
				System.out.println("ConnectionId: " + connectionId + " cmdID: " + cmdId + " payLoad: " + Arrays.toString(payLoad));
			}
		});
		llc.init(config);
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
