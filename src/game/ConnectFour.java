package game;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import network.Client;
import players.Player;
import views.GUI;
import views.TUI;
import views.View;

public class ConnectFour extends Observable{
	
	public ConnectFour() {
		View v;
		String ui = View.askWhichUI();
		switch(ui) {
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
		
		String gameType = v.askLocalOrOnline();
		if(gameType.equals("local")) {
			Player players[] = v.askForPlayers();
			this.setChanged();
			this.notifyObservers("playersAsked");
			this.createNewGame(players, v);
		} else if(gameType.equals("online")) {
			InetAddress host=null;
			try {
				host = InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				this.setChanged();
				this.notifyObservers("badHost");
			}
			
			try {
				new Thread(new Client(host, 2727, v)).start();
			} catch (IOException e) {
				this.setChanged();
				this.notifyObservers("cannotCreateClient");
			}
		}
	}
	
	public ConnectFour(View v) {
		this.addObserver(v);
		String gameType = v.askLocalOrOnline();
		if(gameType.equals("local")) {
			Player players[] = v.askForPlayers();
			this.setChanged();
			this.notifyObservers("playersAsked");
			this.createNewGame(players, v);
		} else if(gameType.equals("online")) {
			System.out.println("jajaja");
			InetAddress host=null;
			try {
				host = InetAddress.getByName("localhost");
			} catch (UnknownHostException e) {
				this.setChanged();
				this.notifyObservers("badHost");
			}
			
			try {
				new Thread(new Client(host, 2727, v)).start();
			} catch (IOException e) {
				this.setChanged();
				this.notifyObservers("cannotCreateClient");
			}
		}
	}
	
	public static void main(String args[])
	{
		new ConnectFour();
	}
	
	public void createNewGame(Player[] players, View v) {
		NormalGame game = new NormalGame(players[0], players[1], v);
	}
}
