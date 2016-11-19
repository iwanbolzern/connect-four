package prg2.connectfour.logic;

import prg2.connectfour.logic.Color;

public class Player {
    private String name;
    private Color color;

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public Color getColor() {
        return this.color;
    }

    public String getName() {
        return this.name;
    }
}