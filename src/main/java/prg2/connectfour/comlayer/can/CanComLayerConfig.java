package prg2.connectfour.comlayer.can;

import prg2.connectfour.comlayer.ComLayerConfig;
import prg2.connectfour.comlayer.can.converter.CanBitRate;
import prg2.connectfour.comlayer.can.converter.CanConverter;

public class CanComLayerConfig extends ComLayerConfig {
	
	private String port;
	private SerialBaudRate serialBaudRate;
	private CanConverter canConverter;
	private CanBitRate canBitRate;
	private int dataLength;
	private int ownId;
	
	public String getPort() {
		return this.port;
	}
	
	public void setPort(String port){
		this.port = port;
	}

	public SerialBaudRate getSerialBaudRate() {
		return serialBaudRate;
	}

	public void setSerialBaudRate(SerialBaudRate serialBaudRate) {
		this.serialBaudRate = serialBaudRate;
	}

	public CanBitRate getCanBitRate() {
		return canBitRate;
	}

	public void setCanBitRate(CanBitRate canBaudRate) {
		this.canBitRate = canBaudRate;
	}

	public int getDataLength() {
		// TODO Auto-generated method stub
		return dataLength;
	}
	public void setDataLength(int dataLength){
		this.dataLength = dataLength;
	}
	
	public CanConverter getCanConverter() {
		return canConverter;
	}

	public void setCanConverter(CanConverter canConverter) {
		this.canConverter = canConverter;
	}
	
	public int getOwnId() {
		return ownId;
	}

	public void setOwnId(int ownId) {
		this.ownId = ownId;
	}

}
