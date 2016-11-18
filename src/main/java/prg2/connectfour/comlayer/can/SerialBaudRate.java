package prg2.connectfour.comlayer.can;

public enum SerialBaudRate {
	_300(300),
	_1200(1200),
	_2400(2400),
	_4800(4800),
	_9600(9600),
	_14400(14400),
	_19200(19200),
	_28800(28800),
	_38400(38400),
	_57600(57600),
	_115200(115200),
	_230400(230400);
	
	private int baudRate;
	
	private SerialBaudRate(int baudRate) {
		this.baudRate = baudRate;
	}
	
	public int getRate() {
		return this.baudRate;
	}
}
