package views;
import java.util.Observable;

import network.Client;
import players.Player;


public class GUI implements View {

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getHumanMove(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Player[] askForPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askLocalOrOnline() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askPlayerName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}
}
