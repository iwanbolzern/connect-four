package prg2.connectfour.logic;

/**
 * Represents a player
 *
 * @author Marius Schuler {@literal <binarytenshi@gmail.com>}
 */
public class Player {
    public final Color color;
    public final String name;

    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
    }
}
