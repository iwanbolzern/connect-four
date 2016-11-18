package prg2.connectfour.comlayer;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.coretypes.UInt16;
import prg2.connectfour.comlayer.coretypes.UInt32;

public abstract class LowLevelCommunicator<T extends ComLayerConfig> {
	protected T config;
	private ArrayList<LowLevelCmdReceiver> cmdReceiver = new ArrayList<>();

	public abstract void init(T config);

	public abstract void send(final int connectionId, final byte[] msg);

	public void addCmdReceiver(final LowLevelCmdReceiver receiver) {
		cmdReceiver.add(receiver);
	}

	protected void processMessage(final int connectionId, final byte[] msg) {
		if (msg.length < 2)
			System.out.println("Could not process message: " + new String(msg, config.getUsedCharset()));
		else {
			UInt16 cmdId = UInt16.parse(Arrays.copyOfRange(msg, 0, 2));
			byte[] payLoad = Arrays.copyOfRange(msg, 2, msg.length);

			for (LowLevelCmdReceiver receiver : cmdReceiver) {
				receiver.receive(connectionId, cmdId.getInt(), payLoad);
			}
		}
	}
	
	protected void processControlMessage(ControlCmd cmd) {
		for (LowLevelCmdReceiver receiver : cmdReceiver) {
			receiver.receiveControlCmd(cmd);
		}
	}

	protected void send(final int connectionId, final int cmdId, final byte[] payLoad) {
		byte[] cmd = UInt16.pars(cmdId).getBytes();
		byte[] dataLength = UInt32.parse(payLoad.length + cmd.length).getBytes();
		
		byte[] alldata = ArrayUtils.addAll(dataLength, ArrayUtils.addAll(cmd, payLoad));
		
		String payLoadString = new String(payLoad, config.getUsedCharset());
		String shortVersion = payLoadString.substring(0, payLoadString.length() > 50 ? 50 : payLoadString.length() - 1);
		if(payLoadString.length() > 50)
			shortVersion += "...";
		System.out.println("ComLayer:\tSend ConId: " + connectionId + " cmdId: " + cmdId + " data: " + shortVersion);
		
		send(connectionId, alldata);
	}
}
