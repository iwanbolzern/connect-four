package prg2.connectfour.comlayer.can.converter;

import java.util.ArrayList;

import jssc.SerialPort;
import jssc.SerialPortException;
import prg2.connectfour.comlayer.can.CanComLayerConfig;

public abstract class CanToSerialConverter {
	
	public static CanToSerialConverter getCanConverter(CanConverter converter) {
		switch (converter) {
		case CanUsbW682:
			return new CanUsbW682();
		default:
			throw new IllegalArgumentException("This CanToSerial Converter is not supported");
		}
	}
	
	private ArrayList<DataReceived> dataReceivedListener = new ArrayList<>();
	protected Object lock = new Object();
	private Thread listenThread;
	private boolean isRunning;
	protected SerialPort serialPort;
	
	public void addDataReceivedListener(DataReceived listener) {
		dataReceivedListener.add(listener);
	}
	
	protected void dataReceived(int canId, byte[] data) {
		for(DataReceived listener : dataReceivedListener)
			listener.receiveData(canId, data);
	}

	public void init(CanComLayerConfig config, SerialPort serialPort) throws SerialPortException {
		this.serialPort = serialPort;
		initConverter(config, serialPort);
		startReceive();
	}
	
	protected abstract void initConverter(CanComLayerConfig config, SerialPort serialPort) throws SerialPortException;
	
	public abstract void send(int canId, byte[] data) throws SerialPortException;
	
	public void startReceive() {
		if(!isRunning) {
			isRunning = true;
			listenThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					while (isRunning){
						receive();
					}
				}
			});
			listenThread.start();
		}
	}
	
	protected abstract void receive();
}
