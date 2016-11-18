package prg2.connectfour.comlayer.can;

import prg2.connectfour.comlayer.LowLevelCommunicator;
import prg2.connectfour.comlayer.can.converter.CanToSerialConverter;
import prg2.connectfour.comlayer.can.converter.DataReceived;
import prg2.connectfour.comlayer.coretypes.UInt16;
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

public class CanLowLevelCommunicator extends LowLevelCommunicator<CanComLayerConfig> {

	/*
	 * generate HashMap to acquire data from different measurement nodes As soon
	 * all 128 Values were acquired, the iterator will be reseted.
	 */
	private Map<Integer, Pair<Integer, ArrayList<Byte>>> dic = new HashMap<>();

	private SerialPort serialPort;
	private CanToSerialConverter converter;

	/*
	 * Initializes CanComLayer. This consists of configuring the serial
	 * interface as well as the canToSerial-Converter.
	 */
	@Override
	public void init(CanComLayerConfig config) {
		this.config = config;

		// generate new serialconnection
		serialPort = new SerialPort(config.getPort());

		try {
			serialPort.openPort(); // Open serial port
			serialPort.setParams(config.getSerialBaudRate().getRate(), 8, 1, 0);

			converter = CanToSerialConverter.getCanConverter(config.getCanConverter());
			converter.addDataReceivedListener(new DataReceived() {

				@Override
				public void receiveData(int canId, byte[] data) {
					loggerControllerLogic(canId, data);
				}
			});
			converter.init(config, serialPort);

		} catch (SerialPortException ex) {
			ex.printStackTrace();
		}

	}

	/*
	 * This method distinguishes between a config packet (3bytes) and a data
	 * packet (6bytes)
	 */
	private void loggerControllerLogic(int id, byte[] data) {
		// check for start of transmission
		if (data.length == 3 && data[0] == (byte) 0xFF) {
			// check, if ID is already in theHashMap
			if (dic.containsKey(id)) {
				System.out.println("Detected illegal transmission on CAN_ID: " + id + 
						". New Can transmission started bevor old is finised");
				// TODO: make ControlCmd for illegal transmisstion
				dic.remove(id);
			}

			UInt16 dataLenght = UInt16.parse(Arrays.copyOfRange(data, 1, 3));
			ArrayList<Byte> bytes = new ArrayList<>(dataLenght.getInt());
			dic.put(id, Pair.of(dataLenght.getInt(), bytes));

		} else {
			storeData(id, data);
		}
	}

	private void storeData(int id, byte[] data) {
		// test, if ID already in HashMap
		if (dic.containsKey(id)) {
			for(byte dataByte : data) {
				if(dic.get(id).getRight().size() <= dic.get(id).getLeft()) {
					dic.get(id).getRight().add(dataByte);
				} else {
					System.out.println("More data received on CAN_ID: " + id + 
							". than on startTransmission was defined.");
				}
			}
			if(dic.get(id).getRight().size() >= dic.get(id).getLeft()) {
				Byte[] bytes = dic.get(id).getRight().toArray(new Byte[dic.get(id).getRight().size()]);
				processMessage(id, ArrayUtils.toPrimitive(bytes));
				dic.remove(id);
			}
		} else {
			System.out.println("No start of transmission received on CAN_ID: " + id + 
					". Received Data will be ignored.");
		}

	}

	@Override
	public void send(int connectionId, byte[] msg) {
		final int frameSize = 8;
		ArrayIterator<Byte> itr = new ArrayIterator<Byte>(msg);
		do {
			ArrayList<Byte> subMsg = new ArrayList<>();
			for(int i = 0; i < frameSize && itr.hasNext(); i++)
				subMsg.add(itr.next());
			
			Byte[] subMsgArray = new Byte[subMsg.size()];
			subMsg.toArray(subMsgArray);
			
			try {
				converter.send(config.getOwnId(), ArrayUtils.toPrimitive(subMsgArray));
			} catch (SerialPortException e) {
				e.printStackTrace();
			}
		} while(itr.hasNext());	
	}
}
