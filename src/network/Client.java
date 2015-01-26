package network;

import game.Mark;
import game.NetworkGame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Observable;

import players.ComputerPlayer;
import players.HumanPlayer;
import players.NetworkPlayer;
import players.Player;
import strategies.SmartStrategy;
import views.TUI;
import views.View;


/**
 * Client class for a simple client-server application
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Client extends Observable implements Runnable{
	
	private Socket sock;
	private BufferedReader in;
	private BufferedReader inconsole;
	private BufferedWriter out;
	private NetworkGame game;
	private View UI;
	private String name;

	public Client(InetAddress host, int port, View UI)
			throws IOException {
		
		this.sock = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
    	
    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
    	this.UI = UI;
    	this.addObserver(UI);
    	name = "Roy12";
    	this.UI.setClient(this);
    	((TUI)UI).start();
	}
	
	private void askName() {
		String name = UI.askPlayerName();
		sendMessage("join " + name + " " + 12);
	}
	
	public void run() {
		askName();
		while(true) {
    		try {
    			String line=in.readLine();
    			analyseCommand(line);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
	}
	
	private void analyseCommand(String command) {
		String[] command_split = command.split(" ");
		if(command_split[0].equals("accept") && command_split.length == 2) {
			this.setChanged();
			this.notifyObservers("accepted " + command_split[1]);
			sendMessage("ready_for_game");
		} else if(command_split[0].equals("error")) {
			switch(command_split[1]) {
			case "004":
				this.setChanged();
				this.notifyObservers("nameExists");
				askName();
				break;
			}
		} else if(command_split[0].equals("start_game") && command_split.length == 3) {
			//create players and create networkgame
			
			Player p1=null, p2=null;
			if(command_split[1].equals(name)) {
				p1 = new HumanPlayer(name, Mark.XX);
				p2 = new NetworkPlayer(command_split[2], Mark.OO);
			} else if(command_split[2].equals(name)) {
				p1 = new NetworkPlayer(command_split[1], Mark.XX);
				p2 = new HumanPlayer(name, Mark.OO);
			}
			game = new NetworkGame(p1, p2, UI);
		} else if(command_split[0].equals("request_move") && command_split.length == 2) {
			if(name.equals(command_split[1])) {
				int move = game.askForMove();
				sendMessage("do_move" + " " + move);
			}
		} else if(command_split[0].equals("done_move") && command_split.length == 3) {
			game.moveDone(command_split[1], Integer.parseInt(command_split[2]));
		} else if(command_split[0].equals("game_end") && command_split.length == 1) {
			this.setChanged();
			this.notifyObservers("draw");
		} else if(command_split[0].equals("game_end") && command_split.length == 2) {
			this.setChanged();
			this.notifyObservers("gameOver");
			System.out.println("Game over! The winner is: " + command_split[1]);
		} else if(command_split[0].equals("message")){
			System.out.println(command_split[1] + ": " + command_split[2]);
			game.notifyObservers("gameOver");
		}
	}
	
	/** send a message to a ClientHandler. */
	public void sendMessage(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String readString(String tekst) {
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}

} // end of class Client
