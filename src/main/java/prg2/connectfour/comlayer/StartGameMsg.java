package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Handshake msg to start a game
 * @author Iwan Bolzern <iwan.bolzern@ihomelab.ch>
 */
public class StartGameMsg extends Msg implements Serializable {
    private static final long serialVersionUID = 1930062221249772807L;
    
    private String invitationToken;
    private String gameToken;
    
    /**
     * Invitation token which is responsed with yes
     * @return invitation token with yes response
     */
    public String getInvitationToken() {
        return invitationToken;
    }
    
    /**
     * Sets invitation token which is reponsed with yes
     * @param invitationToken token with yes response
     */
    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }
    
    /**
     * returns secure game token
     * @return secure game token
     */
    public String getGameToken() {
        return gameToken;
    }
    
    /**
     * sets secure game token to identify specific game
     * @param gameToken secure game token
     */
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }
}
