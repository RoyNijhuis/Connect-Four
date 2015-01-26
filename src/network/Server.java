
package network;

import game.Game;
import game.Mark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;

/**
 * Server. 
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class Server {
	private static final String USAGE = "usage: " + Server.class.getName() + " <port>";

	/** Start een Server-applicatie op. */
	public static void main(String[] args) {
		System.out.println("Please enter port: ");
		int port = Integer.parseInt(readString(""));
		
		Server server = new Server(port);
		server.run();
		
	}


	private int port;
	private List<ClientHandler> clients;
	private List<ServerGame> games;
	
    /** Constructs a new Server object */
	public Server(int portArg) {
		this.port = portArg;
		this.clients = new ArrayList<ClientHandler>();
		this.games = new ArrayList<ServerGame>();
	}
	
	public List<ClientHandler> getClients() {
		return clients;
	}
	
	/**
	 * Listens to a port of this Server if there are any Clients that 
	     * would like to connect. For every new socket connection a new
	     * ClientHandler thread is started that takes care of the further
	     * communication with the Client. 
	 */
	public void run() {
		ServerSocket ss = null;
		try {
			ss = new ServerSocket(port);
			while(true) {
				Socket s = ss.accept();
				ClientHandler ch = new ClientHandler(this, s);
				addHandler(ch);
				ch.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void tryToStartGame(ClientHandler client) {
		//kijk of er andere clients zijn
		for(ClientHandler c: clients) {
			if(!c.equals(client) && c.getReady() && client.getReady()) {
				//start game met andere client
				client.sendMessage("debug Je bent verbonden met " + c.getPlayerName());
				c.sendMessage("debug Je bent verbonden met " + client.getPlayerName());
				client.sendMessage("start_game " + client.getPlayerName() + " " + c.getPlayerName());
				c.sendMessage("start_game " + client.getPlayerName() + " " + c.getPlayerName());
				ServerGame game = new ServerGame(client, c);
				client.startGame(game, Mark.XX);
				c.startGame(game, Mark.OO);
				games.add(game);
				game.start();
				client.setReady(false);
				c.setReady(false);
			}
		}
	}
	
	public void broadcastMesGlobal(String msg, String name){
		System.out.println("serverglobal "+msg);
		for(ClientHandler client: clients) {
			client.sendMessage("message "+ name+ " " + msg);
		}
	}
	
	public void broadcastToGame(ServerGame game, String message) {
		for(ServerGame g: games) {
			if(g.equals(game)) {
				
			}
		}
	}
	
	public void print(String message){
		System.out.println(message);
	}
	
	/**
	 * Sends a message using the collection of connected ClientHandlers
	     * to all connected Clients.
	 * @param msg message that is send
	 */
	public void broadcast(String msg) {
		for(ClientHandler client: clients) {
			client.sendMessage(msg);
		}
	}
	
	/**
	 * Add a ClientHandler to the collection of ClientHandlers.
	 * @param handler ClientHandler that will be added
	 */
	public void addHandler(ClientHandler handler) {
		clients.add(handler);
	}
	
	/**
	 * Remove a ClientHandler from the collection of ClientHanlders. 
	 * @param handler ClientHandler that will be removed
	 */
	public void removeHandler(ClientHandler handler) {
		clients.remove(handler);
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
	
} // end of class Server
