package prg2.connectfour.comlayer;

import java.io.Serializable;

public class InvitationMsg extends Msg implements Serializable {
	private static final long serialVersionUID = -4175829652467899771L;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
