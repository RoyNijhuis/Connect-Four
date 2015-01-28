package game;

import network.Client;
import players.Player;
import views.GUI;
import views.TUI;
import views.View;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;


public class ConnectFour extends Observable {
	static Thread[] threads;
	
	/**
     * Deze functie vraagt welke UI gebruikt moet worden en vraagt of online 
     * of lokaal gespeeld wil worden.
     */
	public ConnectFour() {
		View v;
		threads = new Thread[2];
		String ui = View.askWhichUI();
		switch (ui) {
		    case "TUI":
		    	v = new TUI();
		    	break;
		    case "GUI":
		    	v = new GUI();
		    	break;
		    default:
		    	v = null;
		    	break;
		}
		this.addObserver(v);
		
		boolean done = false;
		while (!done) {
			String gameType = v.askLocalOrOnline();
			if (gameType.equals("local")) {
				Player[] players = v.askForPlayers();
				this.setChanged();
				this.notifyObservers("gameStarted");
				done = true;
				this.createNewGame(players, v);
			} else if (gameType.equals("online")) {
				String ip = v.askIPAdress();
				String port = v.askPort();
				int portNumber = Integer.parseInt(port);
				InetAddress host = null;
				try {
					host = InetAddress.getByName(ip);
				} catch (UnknownHostException e) {
					this.setChanged();
					this.notifyObservers("badHost");
				}
				
				try {
					threads[1] = new Thread(new Client(host, portNumber, v));
					threads[1].start();
					done = true;
				} catch (IOException e) {
					this.setChanged();
					this.notifyObservers("cannotCreateClient");
				}
			}
		}
		this.deleteObserver(v);
	}
	
	/** @param v als de game opnieuw gespeeld wil worden, wordt de oude UI meegegeven
     * Deze functie doet hetzelfde als de originele constructor, alleen maakt geen nieuwe UI aan.
     */
	public ConnectFour(View v) {
		v.reset();
		this.addObserver(v);
		boolean done = false;
		while (!done) {
			String gameType = v.askLocalOrOnline();
			if (gameType.equals("local")) {
				Player[] players = v.askForPlayers();
				this.setChanged();
				this.notifyObservers("gameStarted");
				done = true;
				this.createNewGame(players, v);
			} else if (gameType.equals("online")) {
				String ip = v.askIPAdress();
				String port = v.askPort();
				int portNumber = Integer.parseInt(port);
				InetAddress host = null;
				try {
					host = InetAddress.getByName(ip);
				} catch (UnknownHostException e) {
					this.setChanged();
					this.notifyObservers("badHost");
				}
				
				try {
					threads[1] = new Thread(new Client(host, portNumber, v));
					threads[1].start();
					done = true;
				} catch (IOException e) {
					this.setChanged();
					this.notifyObservers("cannotCreateClient");
				}
			}
		}
	}
	
	public static void main(String[] args) {
		new ConnectFour();
	}
	
	/**
     * @param players de spelers van het spel
     * @param v de UI
     * Deze functie maakt een nieuwe NormalGame aan met spelers.
     */
	public void createNewGame(Player[] players, View v) {
		new NormalGame(players[0], players[1], v);
	}
	
	public static void shutdown() {
		for (int i = 0; i < threads.length; i++) {
			if (threads[i] != null) {
				threads[i].interrupt();
				threads[i] = null;
			}
		}
	}
}
