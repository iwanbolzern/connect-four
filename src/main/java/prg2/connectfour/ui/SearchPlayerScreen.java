package prg2.connectfour.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.JPanel;

import prg2.connectfour.comlayer.BasePlayer;
import prg2.connectfour.comlayer.InvitationMsg;
import prg2.connectfour.comlayer.InvitationResponseMsg;
import prg2.connectfour.comlayer.NetworkEnv;

public class SearchPlayerScreen extends JPanel {
	
	private NetworkEnv networkEnv;
	private HashMap<String, BasePlayer> invitationsTokens = new HashMap<>();
	
	private ArrayList<StartGameHandler> startGameListeners = new ArrayList<>();
	
	public SearchPlayerScreen(NetworkEnv env) {
		this.networkEnv = env;
	}
	
	public void init() {
		initComponents();
		startSearch();
	}
	
	public void unregisterAll() {
		
	}
	
	private void initComponents() {
		// init all ui components here
		
		
		// register listeners on network env
		this.networkEnv.addNewPlayerListener(new NetworkEnv.PlayerHandler() {
			@Override
			public void newPlayerDetected(BasePlayer newPlayer) {
				// TODO add player to list
			}
		});
		this.networkEnv.addInvitationListener(new NetworkEnv.InvitationHandler() {
			@Override
			public void invitationReceived(InvitationMsg msg) {
				// show popup on new invitation
				
			}
		});
		this.networkEnv.addInvitationResponseListener(new NetworkEnv.InvitationResponseHandler() {
			@Override
			public void invitationResponseReceived(InvitationResponseMsg msg) {
				if(invitationsTokens.containsKey(msg.getInvitationToken())) {
					onStartGame(msg.getInvitationToken(), invitationsTokens.get(msg.getInvitationToken()));
				}
			}
		});
	}
	
	private void startSearch() {
		this.networkEnv.broadcastHelloMsg();
	}
	
	private void invitePlayer(BasePlayer player) {
		String newToken = this.networkEnv.sendInvitation(player);
		invitationsTokens.put(newToken, player);
	}
	
	private void onStartGame(String gameToken, BasePlayer player) {
		for(StartGameHandler listener : this.startGameListeners)
			listener.startGame(gameToken, player);
	}
	
	public void addStartGameListener(StartGameHandler listener) {
		startGameListeners.add(listener);
	}
	
	public interface StartGameHandler {
		void startGame(String gameToken, BasePlayer player);
	}
}
