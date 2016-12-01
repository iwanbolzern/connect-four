package prg2.connectfour.comlayer;

import java.io.Serializable;

public class MoveMsg extends Msg implements Serializable {
    private static final long serialVersionUID = -6056126601687864227L;
    
    private int x;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}
	
}
