package prg2.connectfour.comlayer.coretypes;

import java.util.ArrayList;

public class Int24 {
	/** Maximum possible value. */
	public static final int MAX_VALUE = 8388607;
	/** Minimum possible value. */
	public static final int MIN_VALUE = -8388608;
	
	int number;
	private Int24(final int number) {
		this.number = number;
	}

	public int getInt() {
		return this.number;
	}

	public byte[] getBytes() {
		byte[] byteArray = new byte[3];

		byteArray[2] = (byte) (number & 0xFF);
		byteArray[1] = (byte) (number >> 8 & 0xFF);
		byteArray[0] = (byte) (number >> 16);

		return byteArray;
	}

	public static Int24 parse(final byte[] bytes) {
		if (bytes.length > 3)
			throw new IllegalArgumentException("Max size excited. Use just three (3) bytes!");
		
		int intValue = bytes.length > 0 ? bytes[0] : 0;
		for (int i = 1; i < bytes.length; i++) {
			intValue = (intValue << 8) | (bytes[i] & 0xFF);
		}
		
		return new Int24(intValue);
	}
	
	public static Int24 parse(final int number) {
		return new Int24(number);
	}
}
