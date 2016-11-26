package prg2.connectfour.test.comlayer;

import static org.junit.Assert.*;

import org.junit.Test;

import prg2.connectfour.comlayer.coretypes.Int24;

public class CoreTypeUnitTest {

	@Test
	public void int24() {
		int plusmx = Int24.parse(new byte[] { (byte)0b01111111, (byte)0b11111111, (byte)0b11111111 }).getInt();
		assertEquals(plusmx, 8388607);

		int plus = Int24.parse(new byte[] { (byte)0b01111111, (byte)0b11111111, (byte)0b11111110 }).getInt();
		assertEquals(plus, 8388606);

		int zero = Int24.parse(new byte[] { (byte)0b00000000, (byte)0b00000000, 0b00000000 }).getInt();
		assertEquals(zero, 0);

		int minusone = Int24.parse(new byte[] { (byte)0b11111111, (byte)0b11111111, (byte)0b11111111 }).getInt();
		assertEquals(minusone, -1);

		int minus = Int24.parse(new byte[] { (byte)0b10000000, (byte)0b00000000, 0b00000001 }).getInt();
		assertEquals(minus, -8388607);

		int minusmax = Int24.parse(new byte[] { (byte)0b10000000, (byte)0b00000000, 0b00000000 }).getInt();
		assertEquals(minusmax, -8388608);
	}

}
