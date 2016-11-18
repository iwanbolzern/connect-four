package prg2.connectfour.comlayer.coretypes;

public class UInt32 {
	/** Maximum possible value. */
	public static final long MAX_VALUE = 4294967296L;
	/** Minimum possible value. */
	public static final long MIN_VALUE = 0;
	long number;
	
	private UInt32(final long number) {
		this.number = number;
	}
	
	public long getLong() {
		return this.number;
	}
	
	public byte[] getBytes() {
		byte[] byteArray = new byte[4];

		for (int i = 0; i < byteArray.length; i++) {
			byteArray[byteArray.length - i - 1] = (byte) (number >> (i * 8));
		}

		return byteArray;
	}
	
	public static UInt32 parse(final byte[] bytes) {
		if (bytes.length > 4)
			throw new IllegalArgumentException("Max size excited. Use just four (4) bytes!");

		long longValue = bytes.length > 0 ? (bytes[0] & 0xFF) : 0;
		for (int i = 1; i < bytes.length; i++) {
			longValue = (longValue << 8) | (bytes[i] & 0xFF);
		}
		return new UInt32(longValue);
	}
	
	public static UInt32 parse(int number) {
		return new UInt32(number);
	}

}
