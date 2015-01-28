package network;

import game.Mark;
import game.NetworkGame;
import players.NetworkPlayer;
import players.Player;
import views.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

public class Client extends Observable implements Runnable {
	
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private NetworkGame game;
	private View sUI;
	private Player self;
	private String name;
	private String[] extensions;
	private boolean otherHasChat;
	
	/**
     * @param host de host om mee te verbinden
     * @param port de poort van de hostverbinding
     * @param aUI de UI
     * Deze functie maakt een nieuwe Client aan.
     * @throws IOException
     */
	public Client(InetAddress host, int port, View aUI) throws IOException {
		this.sock = new Socket(host, port);
		in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
		out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
    	self = null;
    	this.sUI = aUI;
    	this.addObserver(sUI);
    	name = "";
    	this.sUI.setClient(this);
    	extensions = new String[10];
    	otherHasChat = false;
	}
	
	/**
     * Deze functie laat weten aan de UI dat de naam en het type speler gevraagd moeten worden.
     */
	public void askNameAndType() {
		name = sUI.askPlayerName();
		self = sUI.askType(name);
		sendMessage("join " + name + " " + 12 + " " + "Chat");
	}
	
	/**
     * Deze functie leest continu input van de server
     */
	public void run() {
		askNameAndType();
		boolean running = true;
		while (running) {
    		try {
    			String line = in.readLine();
    			analyseCommand(line);
			} catch (IOException e) {
				try {
					sock.close();
					running = false;
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				sUI.disconnectedError();
			}
    	}
	}
	
	/**
     * @param command Het commando dat ontvangen is van de server
     * Deze functie analyseert elk commando volgens het protocol
     */
	private void analyseCommand(String command) {
		String[] commandSplit = command.split(" ");
		if (commandSplit[0].equals("accept") && commandSplit.length >= 2) {
			for (int i = 2; i < commandSplit.length; i++) {
				extensions[i] = commandSplit[i];
				if (extensions[i].equals("Chat")) {
					otherHasChat = true;
				}
			}
			this.setChanged();
			this.notifyObservers("accepted " + commandSplit[1]);
			sendMessage("ready_for_game");
		} else if (commandSplit[0].equals("error")) {
			switch (commandSplit[1]) {
				case "004":
					this.setChanged();
					this.notifyObservers("nameExists");
					askNameAndType();
					break;
				case "002":
					this.setChanged();
					this.notifyObservers("columnFull");
					break;
			}
		} else if (commandSplit[0].equals("start_game") && commandSplit.length == 3) {
			this.setChanged();
			this.notifyObservers("gameStarted");
			Player p1 = null, p2 = null;
			if (commandSplit[1].equals(name)) {
				p1 = self;
				p1.setMark(Mark.XX);
				p2 = new NetworkPlayer(commandSplit[2], Mark.OO);
			} else if (commandSplit[2].equals(name)) {
				p1 = new NetworkPlayer(commandSplit[1], Mark.XX);
				p2 = self;
				p2.setMark(Mark.OO);
			}
			game = new NetworkGame(p1, p2, sUI);
		} else if (commandSplit[0].equals("request_move") && commandSplit.length == 2) {
			if (name.equals(commandSplit[1])) {
				int move = game.askForMove();
				sendMessage("do_move" + " " + move);
			}
		} else if (commandSplit[0].equals("done_move") && commandSplit.length == 3) {
			game.moveDone(commandSplit[1], Integer.parseInt(commandSplit[2]));
		} else if (commandSplit[0].equals("game_end") && commandSplit.length == 1) {
			this.setChanged();
			this.notifyObservers("draw");
		} else if (commandSplit[0].equals("game_end") && commandSplit.length == 2) {
			game.gameOver(commandSplit[1]);
		} else if (commandSplit[0].equals("message") && commandSplit.length >= 3) {
			this.setChanged();
			this.notifyObservers(command);
		} else {
			sendMessage("error 007");
		}
	}
	
	/**
     * @param msg Het commando van de server
     * Deze functie verstuurt een bericht naar de server
     */
	public void sendMessage(String msg) {
		String result = new String(msg);
		if ((msg.startsWith("chat_local") || msg.startsWith("chat_global")) && !otherHasChat) {
			result = null;
		}
		try {
			if (result != null) {
				out.write(msg + "\n");
				out.flush();
			}
		} catch (IOException e) {
			try {
				sock.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			sUI.disconnectedError();
		}
	}

	/**
     * Sluit de verbindingen af.
     */
	public void shutdown() {
		try {
			sock.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
     * @param tekst De uit te printen tekst
     * Leest van de inputstream.
     * @return String Fe gelezen string
     */
	public static String readString(String tekst) {
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			antw = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (antw == null) ? "" : antw;
	}
}
