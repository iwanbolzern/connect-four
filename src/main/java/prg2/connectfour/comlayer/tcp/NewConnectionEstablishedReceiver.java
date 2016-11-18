package prg2.connectfour.comlayer.tcp;

import java.net.Socket;

public interface NewConnectionEstablishedReceiver {
	public void newConnectionEstablished(Socket socket);
}
