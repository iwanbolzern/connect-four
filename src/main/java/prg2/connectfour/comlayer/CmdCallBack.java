package prg2.connectfour.comlayer;


public abstract class CmdCallBack<T extends PayLoad> {
	
	private Class<T> payLoadClass;
	
	public CmdCallBack(Class<T> payLoadType) {
		this.payLoadClass = payLoadType;
	}
	
	protected Class<T> getPayLoadClass() {
		return this.payLoadClass;
	}
	
	public abstract void receive(int communicationId, T payLoad);
}
