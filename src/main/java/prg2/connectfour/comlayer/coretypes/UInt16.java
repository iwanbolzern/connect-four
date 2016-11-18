package prg2.connectfour.comlayer.coretypes;

public class UInt16 {
	/** Maximum possible value. */
	public static final int MAX_VALUE = 65535;
	/** Minimum possible value. */
	public static final int MIN_VALUE = 0;
	int number;

	private UInt16(final int number) {
		this.number = number;
	}

	public int getInt() {
		return this.number;
	}

	public byte[] getBytes() {
		byte[] byteArray = new byte[2];

		for (int i = 0; i < byteArray.length; i++) {
			byteArray[byteArray.length - i - 1] = (byte) (number >> (i * 8));
		}

		return byteArray;
	}

	public static UInt16 parse(final byte[] bytes) {
		if (bytes.length > 2)
			throw new IllegalArgumentException("Max size excited. Use just two (2) bytes!");

		int intValue = bytes.length > 0 ? (bytes[0] & 0xFF) : 0;
		for (int i = 1; i < bytes.length; i++) {
			intValue = (intValue << 8) | (bytes[i] & 0xFF);
		}
		return new UInt16(intValue);
	}

	public static UInt16 pars(final int number) {
		if (number > MAX_VALUE)
			throw new IllegalArgumentException("Max size excited.");

		return new UInt16(number);
	}

}
