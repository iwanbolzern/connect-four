package prg2.connectfour.logic;

import prg2.connectfour.logic.Color;
import prg2.connectfour.comlayer.BasePlayer;

public class Player extends BasePlayer {
    private Color color;

    public Player(String name, Color color) {
        this.color = color;
        this.setName(name);
    }

    public Color getColor() {
        return this.color;
    }
}