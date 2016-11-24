package prg2.connectfour.comlayer.udp;

import prg2.connectfour.comlayer.LowLevelCommunicator;
import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.controlcmds.ControlCmdReceiver;
import prg2.connectfour.comlayer.coretypes.UInt16;
import prg2.connectfour.comlayer.udp.MsgReceiver;
import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.iterators.ArrayIterator;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

public class UdpLowLevelCommunicator extends LowLevelCommunicator<UDPComLayerConfig> {

	private HashMap<Integer, UdpConnection> udpConnections = new HashMap<>();

	/*
	 * Initializes CanComLayer. This consists of configuring the serial
	 * interface as well as the canToSerial-Converter.
	 */
	@Override
	public void init(UDPComLayerConfig config) {
		this.config = config;
			
		if (config.getConnections() != null) {
			initConnections(config);
		}
	}
	
	private void initConnections(final UDPComLayerConfig config) {
		for (Integer connectionId : config.getConnections().keySet()) {
			if(!udpConnections.containsKey(connectionId)) {
				UdpConnection connection = new UdpConnection();
				connection.setCommunicationId(connectionId);
				registerReceiver(connection);
				connection.init(config.getConnections().get(connectionId));
				udpConnections.put(connectionId, connection);
			} else {
				throw new IllegalArgumentException("It is not possible to have two connections with same id");
			}
		}
	}
	
	private void registerReceiver(final UdpConnection connection) {
		connection.registerMsgReceiver(new MsgReceiver() {

			@Override
			public void msgReceived(byte[] payLoad) {
				processMessage(connection.getCommunicationId(), payLoad);
			}
		});
		connection.registerControlReceiver(new ControlCmdReceiver() {
			
			@Override
			public void receiveControlCmd(ControlCmd cmd) {
				processControlMessage(cmd);				
			}
		});
	}

	private int getNewConId() {
		Integer key = 0;
		while (key++ < Integer.MAX_VALUE) {
			if (!udpConnections.containsKey(key))
				return key;
		}
		System.out.println("Attention you've to many connections");
		return 0;
	}

	@Override
	public void send(int connectionId, byte[] msg) {
		if (udpConnections.containsKey(connectionId)) {
			udpConnections.get(connectionId).sendMessage(msg);
		} else {
			System.out.println("Connection with id " + connectionId + " not known");
		}
	}
}
