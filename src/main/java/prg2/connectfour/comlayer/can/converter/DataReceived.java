package prg2.connectfour.comlayer.can.converter;

public interface DataReceived {
	public void receiveData(int canId, byte[] data);
}
