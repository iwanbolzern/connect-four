package prg2.connectfour.comlayer;

import java.io.Serializable;

public class StartGameMsg extends Msg implements Serializable {
    private static final long serialVersionUID = 1930062221249772807L;
    
    private int x;
	private int y;
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
