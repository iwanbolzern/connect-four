package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Response for InvitationMsg
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class InvitationResponseMsg extends Msg implements Serializable {

	private static final long serialVersionUID = 4047898903287630620L;

	private String invitationToken;
	private boolean invitationAccepted;
	
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
