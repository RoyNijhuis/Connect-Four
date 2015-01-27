package game;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import network.Client;
import players.*;
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
		
		boolean done = false;
		while(!done) {
			String gameType = v.askLocalOrOnline();
			if(gameType.equals("local")) {
				Player players[] = v.askForPlayers();
				this.setChanged();
				this.notifyObservers("gameStarted");
				done = true;
				this.createNewGame(players, v);
			} else if(gameType.equals("online")) {
				InetAddress host=null;
				try {
					host = InetAddress.getByName("spitfire.student.utwente.nl");
				} catch (UnknownHostException e) {
					this.setChanged();
					this.notifyObservers("badHost");
				}
				
				try {
					new Thread(new Client(host, 2003, v)).start();
					System.out.println("komt langs");
					done = true;
				} catch (IOException e) {
					this.setChanged();
					this.notifyObservers("cannotCreateClient");
				}
			}
		}
	}
	
	public ConnectFour(View v) {
		this.addObserver(v);
		
		boolean done = false;
		while(!done) {
			System.out.println("in loop");
			String gameType = v.askLocalOrOnline();
			System.out.println("ask");
			if(gameType.equals("local")) {
				Player players[] = v.askForPlayers();
				this.setChanged();
				this.notifyObservers("gameStarted");
				done = true;
				this.createNewGame(players, v);
			} else if(gameType.equals("online")) {
				InetAddress host=null;
				try {
					host = InetAddress.getByName("spitfire.student.utwente.nl");
				} catch (UnknownHostException e) {
					this.setChanged();
					this.notifyObservers("badHost");
				}
				
				try {
					new Thread(new Client(host, 2003, v)).start();
					System.out.println("komt langs");
					done = true;
				} catch (IOException e) {
					this.setChanged();
					this.notifyObservers("cannotCreateClient");
				}
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
