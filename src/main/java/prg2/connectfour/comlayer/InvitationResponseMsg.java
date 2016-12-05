package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Response for InvitationMsg
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class InvitationResponseMsg extends InvitationMsg implements Serializable {

	private static final long serialVersionUID = 4047898903287630620L;

	private boolean invitationAccepted;

	/**
	 * Returns if invitation is accepted by the opponent
	 * @return if invitation is accepted
	 */
	public boolean isInvitationAccepted() {
		return invitationAccepted;
	}

	/** 
	 * Sets if invitation is accepted
	 * @param invitationAccepted if invitation is accepted
	 */
	public void setInvitationAccepted(boolean invitationAccepted) {
		this.invitationAccepted = invitationAccepted;
	}
}
