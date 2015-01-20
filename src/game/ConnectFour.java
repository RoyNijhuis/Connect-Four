package game;
import java.util.Observable;

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
		Player players[] = v.askForPlayers();
		this.setChanged();
		this.notifyObservers("playersAsked");
		this.createNewGame(players, v);
	}
	
	public static void main(String args[])
	{
		new ConnectFour();
	}
	
	public void createNewGame(Player[] players, View v) {
		Game game = new Game(players[0], players[1], v);
	}
}
