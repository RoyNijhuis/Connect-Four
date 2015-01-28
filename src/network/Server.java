
package network;

import game.Mark;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import views.TUIServer;

/**
 * Server. 
 * @author  Roy en Edwin
 * @version 2015.01.28
 */
public class Server {
	private TUIServer sUI;
	
	/** Start een Server-applicatie op. */
	public static void main(String[] args) {
		TUIServer uIServer = new TUIServer();
		Server server = null;
		while (server == null) {
			int port = Integer.parseInt(uIServer.readString("Enter a port number"));
			try {
				server = new Server(port, uIServer);
			} catch (IOException e) {
				uIServer.message("This port is already being used.");
			}
		}
		uIServer.message("The server is running and can accept clients!");
		server.run();
	}


	private int port;
	private List<ClientHandler> clients;
	private List<ServerGame> games;
	private ServerSocket socket;
	
	/**
     * Constructs a new Server object. 
     * @param portArg de poort
     * @param zUI de UI
     * throws IOException
     */
	public Server(int portArg, TUIServer zUI) throws IOException {
		this.port = portArg;
		this.clients = new ArrayList<ClientHandler>();
		this.games = new ArrayList<ServerGame>();
		this.socket = new ServerSocket(port);
		this.sUI = zUI;
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
		try {
			while (true) {
				Socket s = socket.accept();
				ClientHandler ch = new ClientHandler(this, s, sUI);
				addHandler(ch);
				ch.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void tryToStartGame(ClientHandler client) {
		for (ClientHandler c: clients) {
			if (!c.equals(client) && c.getReady() && client.getReady()) {
				client.sendMessage("start_game " 
								+ client.getPlayerName() + " " + c.getPlayerName());
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
	
	public void broadcastMesGlobal(String msg, String name) {
		sUI.message("global message: " + name + ": " + msg);
	        
		for (ClientHandler client: clients) {
			if (client.getChat()) {
				client.sendMessage("message " + name + " " + msg);
			}
		}
	}
	
	public void broadcastToGame(ServerGame game, String message, String name) {
		for (ClientHandler client: game.getClients()) {
			client.sendMessage("message " + name + " " + message);
		}
	}
	
	public void leftGame(ServerGame game, ClientHandler c) {
		sUI.message("Player left: " + c.getPlayerName());
		
		for (ClientHandler client: game.getClients()) {
			if (!client.equals(c)) {
				client.sendMessage("game_end " + client.getPlayerName());
			}
			
		}
		clients.remove(c);
		game.terminate();
		games.remove(game);
	}
	
	public void removeGame(ServerGame game) {
		games.remove(game);
	}
	/**
	 * Sends a message using the collection of connected ClientHandlers
	     * to all connected Clients.
	 * @param msg message that is send
	 */
	public void broadcast(String msg) {
		for (ClientHandler client: clients) {
			if (client.getChat()) {
				client.sendMessage(msg);
			}
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
}
