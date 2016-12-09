package prg2.connectfour.comlayer;

import java.io.Serializable;

public class StartGameMsg extends Msg implements Serializable {
    private static final long serialVersionUID = 1930062221249772807L;
    
    private String invitationToken;
    private String gameToken;
    
    public String getInvitationToken() {
        return invitationToken;
    }
    public void setInvitationToken(String invitationToken) {
        this.invitationToken = invitationToken;
    }
    public String getGameToken() {
        return gameToken;
    }
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }
}
