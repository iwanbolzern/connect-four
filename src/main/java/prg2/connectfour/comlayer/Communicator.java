package prg2.connectfour.comlayer;

import java.util.ArrayList;
import java.util.HashMap;

import prg2.connectfour.comlayer.can.CanComLayerConfig;
import prg2.connectfour.comlayer.can.CanLowLevelCommunicator;
import prg2.connectfour.comlayer.controlcmds.ControlCmd;
import prg2.connectfour.comlayer.tcp.TcpComLayerConfig;
import prg2.connectfour.comlayer.tcp.TcpLowLevelCommunicator;

import com.google.gson.Gson;

/**
 * Main class for a command based communication Layer
 * @author tabolzer
 * 
 */
public class Communicator implements LowLevelCmdReceiver {
	private static final int ControlCmdComId = -1;

	private LowLevelCommunicator llCommunicator;
	private HashMap<Integer, ArrayList<CmdCallBack<? extends PayLoad>>> callBackList = new HashMap<>();
	private ComLayerConfig config;

	public void init(final ComLayerConfig config) {
		this.config = config;
		if (config instanceof TcpComLayerConfig) {
			llCommunicator = new TcpLowLevelCommunicator();
		} else if (config instanceof CanComLayerConfig) {
			llCommunicator = new CanLowLevelCommunicator();
		} else {
			throw new IllegalArgumentException("Config of Type " + config.getClass().getSimpleName() + " not known");
		}
		llCommunicator.addCmdReceiver(this);
		llCommunicator.init(config);
	}

	public void registerCmd(final int cmdId, final CmdCallBack<? extends PayLoad> callBack) {
		if (callBackList.containsKey(cmdId)) {
			callBackList.get(cmdId).add(callBack);
		} else {
			callBackList.put(cmdId, new ArrayList<CmdCallBack<?>>() {
				{
					add(callBack);
				}
			});
		}
	}

	public void send(final int connectionId, final int cmdId, final PayLoad payLoad) {
		llCommunicator.send(connectionId, cmdId, payLoad.getBytes(config.getUsedCharset()));
	}

	@Override
	public void receive(final int connectionId, final int cmdId, final byte[] payLoad) {
		if (!callBackList.containsKey(cmdId))
			System.out.println("No CallBack registered for cmd with id " + cmdId);
		else {
			for (CmdCallBack callBack : callBackList.get(cmdId)) {
				PayLoad payloadObject;
				try {
					payloadObject = (PayLoad) callBack.getPayLoadClass().newInstance();
					payloadObject = payloadObject.parse(payLoad, callBack.getPayLoadClass(), config.getUsedCharset());
					callBack.receive(connectionId, payloadObject);
				} catch (InstantiationException | IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void receiveControlCmd(ControlCmd cmd) {
		if (!callBackList.containsKey(cmd.getCMD_ID()))
			System.out.println("No ControlCallBack registered for cmd with id " + cmd.getCMD_ID());
		else {
			for (CmdCallBack callBack : callBackList.get(cmd.getCMD_ID())) {
				callBack.receive(ControlCmdComId, cmd);
			}
		}
	}
}
