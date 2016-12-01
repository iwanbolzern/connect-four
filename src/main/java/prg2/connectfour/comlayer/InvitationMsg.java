package prg2.connectfour.comlayer;

import java.io.Serializable;

public class InvitationMsg extends Msg implements Serializable {
	private static final long serialVersionUID = -4175829652467899771L;
	
	private String name;
	private String invitationToken;
	private int x;
	private int y;
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getInvitationToken() {
		return invitationToken;
	}

	public void setInvitationToken(String token) {
		this.invitationToken = token;
	}
	
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
