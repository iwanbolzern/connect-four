package prg2.connectfour.comlayer.controlcmds.tcp;

import prg2.connectfour.comlayer.controlcmds.ControlCmd;

public class TcpConnectedCmd extends ControlCmd {

	private int communicationId;
	
	public int getCommunicationId() {
		return communicationId;
	}

	public void setCommunicationId(int communicationId) {
		this.communicationId = communicationId;
	}

	@Override
	public int getCMD_ID() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
