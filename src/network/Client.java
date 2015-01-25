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
public class Client extends Thread{
	
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private NetworkGame game;
	private View UI;
	private String name;

	public Client(InetAddress host, int port)
			throws IOException {
		
		this.sock = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
    	out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
    	this.UI = new TUI();
    	name = "Roy12";
	}
	
	public void run() {
		sendMessage("join " + name + " " + 12);
		
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
			System.out.println("You are accepted!(group number: " + command_split[1] + ")");
			sendMessage("ready_for_game");
		} else if(command_split[0].equals("debug")) {
			for(int i=1; i<command_split.length;i++) {
				System.out.print(command_split[i] + " ");
			}
			System.out.print("\n");
		} else if(command_split[0].equals("error")) {
			for(int i=1; i<command_split.length;i++) {
				System.out.print(command_split[i] + " ");
			}
			System.out.print("\n");
		} else if(command_split[0].equals("start_game") && command_split.length == 3) {
			//create players and create networkgame
			
			Player p1=null, p2=null;
			if(command_split[1].equals(name)) {
				p1 = new ComputerPlayer(name, Mark.XX, new SmartStrategy());
				p2 = new NetworkPlayer(command_split[2], Mark.OO);
			} else if(command_split[2].equals(name)) {
				p1 = new NetworkPlayer(command_split[1], Mark.XX);
				p2 = new ComputerPlayer(name, Mark.OO, new SmartStrategy());
			}
			game = new NetworkGame(p1, p2, UI);
		} else if(command_split[0].equals("request_move") && command_split.length == 2) {
			if(name.equals(command_split[1])) {
				System.out.println("move requested");
				int move = game.askForMove();
				sendMessage("do_move" + " " + move);
				System.out.println("move submitted");
			}
		} else if(command_split[0].equals("done_move") && command_split.length == 3) {
			game.moveDone(command_split[1], Integer.parseInt(command_split[2]));
			System.out.println("move done by " + command_split[1] + ": " + command_split[2]);
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
	
	private static void print(String message){
		System.out.println(message);
	}
	
	public static String readString(String tekst) {
		System.out.print(tekst);
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
