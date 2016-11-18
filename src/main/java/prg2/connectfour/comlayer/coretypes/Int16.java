package prg2.connectfour.comlayer.coretypes;

public class Int16 {
	/** Maximum possible value. */
	public static final short MAX_VALUE = 32767;
	/** Minimum possible value. */
	public static final short MIN_VALUE = -32768;
	
	short number;
	private Int16(final short number) {
		this.number = number;
	}

	public int getShort() {
		return this.number;
	}

	public byte[] getBytes() {
		byte[] byteArray = new byte[2];

		for (int i = 1; i < byteArray.length + 1; i++) {
			byteArray[byteArray.length - i] = (byte) (number >> (i * 8));
		}

		return byteArray;
	}

	public static Int16 parse(final byte[] bytes) {
		if (bytes.length > 2)
			throw new IllegalArgumentException("Max size excited. Use just two (2) bytes!");

		short shortValue = bytes.length > 0 ? bytes[0] : 0;
		for (int i = 1; i < bytes.length; i++) {
			shortValue = (short)((shortValue << 8) | (bytes[i] & 0xFF));
		}

		return new Int16(shortValue);
	}
}
