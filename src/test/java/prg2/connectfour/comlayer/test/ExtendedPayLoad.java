package prg2.connectfour.comlayer.test;
import java.nio.charset.Charset;

import prg2.connectfour.comlayer.PayLoad;


public class ExtendedPayLoad extends PayLoad {
	private String label;
	private int number;
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
}
