package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Message which is sent to a possible opponent
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class InvitationMsg extends Msg implements Serializable {
	private static final long serialVersionUID = -4175829652467899771L;
	
	private String invitationToken;
	private int x;
	private int y;
	
	/**
	 * Returns token for the given invitation.
	 * This token is used to prevent fake invitations.
	 * @return secure invitation token
	 */
	public String getInvitationToken() {
		return invitationToken;
	}

	/**
	 * Sets secure invitation token
	 * @param secure invitation token
	 */
	public void setInvitationToken(String token) {
		this.invitationToken = token;
	}
	
	/**
	 * Returns the width of the playground which is to be used
	 * @return width of playground
	 */
	public int getX() {
		return x;
	}

	/**
	 * Sets width of playground
	 * @param x width of palyground
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Returns height of playground which is to be used
	 * @return height of playground
	 */
	public int getY() {
		return y;
	}

	/**
	 * Sets height of playground
	 * @param y heigt of playground
	 */
	public void setY(int y) {
		this.y = y;
	}
}
