package prg2.connectfour.comlayer.can.converter;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import jssc.SerialPort;
import jssc.SerialPortException;
import prg2.connectfour.comlayer.can.CanComLayerConfig;

public class CanUsbW682 extends CanToSerialConverter {

	@Override
	public void initConverter(CanComLayerConfig config, SerialPort serialPort) throws SerialPortException {
		flushCan(serialPort);
		setCanBitRate(config.getCanBitRate(), serialPort);

		/*
		 * Open the CAN channel. This command is only active if the CAN channel
		 * is closed and has been set up prior with either the S or s command
		 * (i.e. initiated).
		 */
		serialPort.writeBytes("O\r".getBytes());
	}

	/*
	 * Converter specific! Always start each session (when your program starts)
	 * with sending 2-3 [CR] to empty any prior command or queued character in
	 * the CANUSB (many times at power up there could be false characters in the
	 * queue or old ones that was from a previous session and make sure you wait
	 * for CR and/or BELL responce back, ther same ammount as you sent CR out)
	 */
	private void flushCan(SerialPort serialPort) throws SerialPortException {
		for (int i = 0; i < 5; i++) {
			serialPort.writeBytes("\r".getBytes());
		}
	}

	private void setCanBitRate(CanBitRate bitRate, SerialPort serialPort) throws SerialPortException {
		switch (bitRate) {
		case _10:
			serialPort.writeBytes("S0\r".getBytes());
			break;
		case _20:
			serialPort.writeBytes("S1\r".getBytes());
			break;
		case _50:
			serialPort.writeBytes("S2\r".getBytes());
			break;
		case _100:
			serialPort.writeBytes("S3\r".getBytes());
			break;
		case _125:
			serialPort.writeBytes("S4\r".getBytes());
			break;
		case _250:
			serialPort.writeBytes("S5\r".getBytes());
			break;
		case _500:
			serialPort.writeBytes("S6\r".getBytes());
			break;
		case _800:
			serialPort.writeBytes("S7\r".getBytes());
			break;
		case _1000:
			serialPort.writeBytes("S8\r".getBytes());
			break;
		default:
			System.out.println("CanBitRate is not supported");
		}
	}

	@Override
	public void send(int canId, byte[] data) throws SerialPortException {
		String frame = "t" + String.format("%03X", canId) + data.length;
		frame += byteArrayToHexString(data);
		frame += "\r";
		
		serialPort.writeBytes(frame.getBytes());
		System.out.println("######## was written to Serial Port: " + frame);
	}

	/*
	 * receive CAN-Data in the format: t10021133[CR] ( as HexString)
	 * StartOfPacket 	= string[0] 	= 't' 
	 * Identifier 		= string[1-4] 	= '100' 	= 0x100 
	 * length 			= string[4] 	= '2'		= 2 bytes
	 * data 			= string[5-end] = '1133'	= '0x11, 0x33'
	 */
	@Override
	protected void receive() {
		try {
			if (serialPort.readString(1).equals("t")) {
				String idString = serialPort.readString(3);
				int id = Integer.parseInt(idString, 16);
				String lengeString = serialPort.readString(1);
				int length = Integer.parseInt(lengeString, 16);
				byte[] data = hexStringToByteArray(serialPort.readString(2 * length));
				synchronized (lock) {
					dataReceived(id, data);
				}
			}
		} catch (SerialPortException ex) {
			System.out.println(ex);
		}
	}

	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) | Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}
	
	private static String byteArrayToHexString(byte[] array) {
		String byteString = "";
		for(byte oneByte : array) {
			byteString = byteString + String.format("%02X", oneByte);
		}
		return byteString;
	}
}
