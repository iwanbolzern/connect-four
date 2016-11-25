package prg2.connectfour;

import java.awt.Dimension;

import javax.swing.JFrame;

import prg.connectfour.ui.PlayGround;
import prg2.connectfour.comlayer.NetworkEnv;

public class ConnectFour extends JFrame {
	
	private NetworkEnv networkEnv;
	
	public ConnectFour() {
		add(new PlayGround(10, 10));
	}
	
	
	public static void main(String[] args) {
		JFrame.setDefaultLookAndFeelDecorated(true);
	    JFrame mainFrame = new ConnectFour();
	    mainFrame.setTitle("Connect four - Have fun");
	    mainFrame.setPreferredSize(new Dimension(400, 200));
	    mainFrame.pack();
	    mainFrame.setVisible(true);
	}
}
