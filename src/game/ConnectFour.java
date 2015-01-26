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
			v = new TUI(null);
			break;
		case "GUI":
			v = new GUI();
			break;
		default:
			v = null;
			break;
		}
		this.addObserver(v);
		/*Player players[] = v.askForPlayers();
		this.setChanged();
		this.notifyObservers("playersAsked");
		this.createNewGame(players, v);*/
		
		InetAddress host=null;
		try {
			host = InetAddress.getByName("localhost");
		} catch (UnknownHostException e) {
			this.notifyObservers("badHost");
		}
		
		try {
			new Client(host, 2727).start();
		} catch (IOException e) {
			this.notifyObservers("cannotCreateClient");
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
