package prg2.connectfour.logic;

import prg2.connectfour.logic.Color;

public class Player {
    private Color color;
    private String name;

    public Player(String name, Color color) {
        this.color = color;
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public Color getColor() {
        return this.color;
    }
}