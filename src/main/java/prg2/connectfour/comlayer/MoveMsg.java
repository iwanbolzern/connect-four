package prg2.connectfour.comlayer;

import java.io.Serializable;

/**
 * Message which is sent for every drop (move) during the game
 * @author Iwan Bolzern {@literal <iwan.bolzern@ihomelab.ch>}
 */
public class MoveMsg extends Msg implements Serializable {
    private static final long serialVersionUID = -6056126601687864227L;
    
    private String gameToken;
    private int x;

    /**
     * Returns the x coordinate of the drop (move)
     * @return x coordinate of the drop
     */
	public int getX() {
		return x;
	}

	/**
	 * Sets x coordinate of the drop (move)
	 * @param x coordinate ot the drop (move)
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Return the token for a secure game
	 * @return Gametoken
	 */
	public String getGameToken() {
        return gameToken;
    }

	/**
	 * Sets the token for a secure game
	 * @param secure game token
	 */
    public void setGameToken(String gameToken) {
        this.gameToken = gameToken;
    }
	
}
