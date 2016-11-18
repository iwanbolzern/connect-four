package prg2.connectfour.comlayer.coretypes;

public class LongByteArray {
	
	private static int maxArrayLength = 1073741824; //2^30
	
	/**
	 * Creats an array with the structure [0][0...max], [1][0...max]
	 * @param size
	 * @return
	 */
	public static byte[][] getLongArray(long size) {
		int bigSize = (int)size / maxArrayLength;
		byte[][] array = new byte[bigSize + 1][];
		for(int i = 0; i < bigSize; i++)
			array[i] = new byte[maxArrayLength];
		array[bigSize] = new byte[(int)size % maxArrayLength];
		return array;
	}
}
