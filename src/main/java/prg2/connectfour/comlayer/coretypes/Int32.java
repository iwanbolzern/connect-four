package prg2.connectfour.comlayer.coretypes;

public class Int32 {
	/** Maximum possible value. */
	public static final int MAX_VALUE = 2147483647;
	/** Minimum possible value. */
	public static final int MIN_VALUE = -2147483648;
	
	int number;
	private Int32(final int number) {
		this.number = number;
	}

	public int getInt() {
		return this.number;
	}

	public byte[] getBytes() {
		byte[] byteArray = new byte[4];

		for (int i = 1; i < byteArray.length + 1; i++) {
			byteArray[byteArray.length - i] = (byte) (number >> (i * 8));
		}

		return byteArray;
	}

	public static Int32 parse(final byte[] bytes) {
		if (bytes.length > 4)
			throw new IllegalArgumentException("Max size excited. Use just four (4) bytes!");

		int intValue = bytes.length > 0 ? bytes[0] : 0;
		for (int i = 1; i < bytes.length; i++) {
			intValue = (intValue << 8) | (bytes[i] & 0xFF);
		}

		return new Int32(intValue);
	}
}
