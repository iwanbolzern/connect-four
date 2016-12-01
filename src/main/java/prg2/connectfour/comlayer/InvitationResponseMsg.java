package prg2.connectfour.comlayer;

import java.io.Serializable;

public class InvitationResponseMsg extends Msg implements Serializable {

	private static final long serialVersionUID = 4047898903287630620L;

	private String invitationToken;
	private boolean invitationAccepted;

	public String getInvitationToken() {
		return invitationToken;
	}

	public void setInvitationToken(String invitationToken) {
		this.invitationToken = invitationToken;
	}

	public boolean isInvitationAccepted() {
		return invitationAccepted;
	}

	public void setInvitationAccepted(boolean invitationAccepted) {
		this.invitationAccepted = invitationAccepted;
	}
}
