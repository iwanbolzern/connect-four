package prg2.connectfour.comlayer.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

public class TcpServer {
	private static final int socketTimeOut = 1000;
	
	private ServerSocket serverSocket;
	private Thread listenThread;
	private ArrayList<NewConnectionEstablishedReceiver> newConReceivers = new ArrayList<>();
	private boolean isRunning;
	private Object lock = new Object();
	
	public void init(final TcpServerConfig config) throws IOException {
		serverSocket = new ServerSocket(config.getPort());
		serverSocket.setSoTimeout(socketTimeOut);
	}
	
	public void addNewConnectionReceiver(final NewConnectionEstablishedReceiver newConReceiver) {
		this.newConReceivers.add(newConReceiver);
	}
	
	public void start() {
		if(!isRunning) {
			isRunning = true;
			listenThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					listenForConnections();
				}
			});
			listenThread.start();
		}
	}
	
	public void stop() {
		isRunning = false;
	}
	
	private void listenForConnections() {
		while(isRunning) {
			try {
				Socket connectionSocket = serverSocket.accept();
				synchronized(lock)
		        {
					for(NewConnectionEstablishedReceiver receiver : newConReceivers) {
						receiver.newConnectionEstablished(connectionSocket);
					}
		        }
			} catch (SocketTimeoutException e) {
				//Do Nothing
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}
}
