package prg2.connectfour.comlayer;

import java.io.Serializable;

public class InvitationResponseMsg extends Msg implements Serializable {

	private static final long serialVersionUID = 4047898903287630620L;

	private boolean invitationAccepted;

	public boolean isInvitationAccepted() {
		return invitationAccepted;
	}

	public void setInvitationAccepted(boolean invitationAccepted) {
		this.invitationAccepted = invitationAccepted;
	}
}
