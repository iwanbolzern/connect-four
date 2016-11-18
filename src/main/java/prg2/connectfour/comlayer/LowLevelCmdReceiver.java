package prg2.connectfour.comlayer;

import prg2.connectfour.comlayer.controlcmds.ControlCmd;

public interface LowLevelCmdReceiver {
	public void receive(final int connectionId, final int cmdId, final byte[] payLoad);
	
	public void receiveControlCmd(final ControlCmd cmd);
}
