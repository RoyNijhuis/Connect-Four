package network;

import game.Mark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * ClientHandler.
 * @author  Theo Ruys
 * @version 2005.02.21
 */
public class ClientHandler extends Thread {

	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;
	private boolean ready_to_start_game;
	private ServerGame game;
	private Mark mark;
	private int move;

	/**
	 * Constructs a ClientHandler object
	 * Initialises both Data streams.
	 *@ requires server != null && sock != null;
	 */
	public ClientHandler(Server serverArg, Socket sockArg) throws IOException {
		this.server = serverArg;
		this.sock = sockArg;
		this.in = new BufferedReader(new InputStreamReader(sockArg.getInputStream(), "UTF-8"));
    	this.out = new BufferedWriter(new OutputStreamWriter(sockArg.getOutputStream(), "UTF-8"));
    	ready_to_start_game=false;
    	game = null;
    	mark = null;
    	move = -1;
	}
	
	public String getPlayerName() {
		return clientName;
	}
	
	public Mark getMarkInCurrentGame() {
		return mark;
	}
	
	public void broadcastDraw() {
		sendMessage("game_end");
	}
	
	public void broadcastWinner(ClientHandler winner) {
		sendMessage("game_end " + winner.getPlayerName());
	}

	/**
         * This method takes care of sending messages from the Client.
         * Every message that is received, is preprended with the name
         * of the Client, and the new message is offered to the Server
         * for broadcasting. If an IOException is thrown while reading
         * the message, the method concludes that the socket connection is
         * broken and shutdown() will be called. 
	 */
	public void run() {
		boolean running = true;
		while(running) {
			try {
				String inputString = in.readLine();
				String[] input = inputString.split(" ");
				System.out.println(inputString);
				if(input[0].equals("join") && input.length == 3) {
					clientName = input[1];
					int group = Integer.parseInt(input[2]);
					int playersWithSameName=0;
					//check name
					for(ClientHandler c: server.getClients()) {
						if(c.getPlayerName().equals(clientName)) {
							playersWithSameName++;
						}
					}
					if(playersWithSameName == 1) {
						sendMessage("accept 12");
					} else {
						sendMessage("error 004");
						shutdown();
						running = false;
						break;
					}
				} else if(input[0].equals("ready_for_game") && input.length == 1) {
					ready_to_start_game = true;
					server.tryToStartGame(this);
				} else if(input[0].equals("do_move")) {
					move = Integer.parseInt(input[1]);
				} else if(input[0].equals("chat_global")){
					String message[] = inputString.split(" ", 2);
					server.broadcastMesGlobal(message[1], clientName);
				}
			} catch (IOException e) {
				shutdown();
				break;
			}
		}
	}
	
	public void requestMove(ClientHandler c) {
		sendMessage("request_move " + c.getPlayerName());
	}
	
	public void broadCastMove(ClientHandler player, int move) {
		sendMessage("done_move " + player.getPlayerName() + " " + move);
	}
	
	public int getMove() {
		int result = -1;
		while(move == -1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		result = move;
		move = -1;
		return result;
	}
	
	public void startGame(ServerGame g, Mark m) {
		this.game = g;
		this.mark = m;
	}
	
	public boolean getReady() {
		return ready_to_start_game;
	}
	
	public void setReady(boolean r) {
		this.ready_to_start_game = r;
	}

	/**
	 * This method can be used to send a message over the socket
         * connection to the Client. If the writing of a message fails,
         * the method concludes that the socket connection has been lost
         * and shutdown() is called.
	 */
	public void sendMessage(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			shutdown();
		}
	}

	/**
	 * This ClientHandler signs off from the Server and subsequently
         * sends a last broadcast to the Server to inform that the Client
         * is no longer participating in the chat. 
	 */
	private void shutdown() {
		server.removeHandler(this);
	}

} // end of class ClientHandler
