package prg2.connectfour.ui;


import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;

public class HomeScreen extends JPanel {
	private ArrayList<PlayHandler> playListeners = new ArrayList<>();
	
	private JButton networkButton;
	private JButton singleButton;
	private JButton loadGameButton;
	
	public HomeScreen() {
		
		// Place buttons here
		
	}
	
	private void onPlayClickt(GameMode mode, String playerName) {
		for(PlayHandler listener : playListeners)
			listener.onPlayClickt(mode, playerName);
	}
	
	public void addPlayListener(PlayHandler listener) {
		playListeners.add(listener);
	}
	
	public interface PlayHandler {
		void onPlayClickt(GameMode mode, String playerName);
	}
	
	public enum GameMode {
		NETWORK,
		SINGLE,
		LOAD_GAME
	}
}
