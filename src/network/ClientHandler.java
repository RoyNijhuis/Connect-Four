package network;

import game.Mark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import views.TUIServer;

/**
 * Board. 
 * @author  Roy & Edwin
 * @version 2015.01.28
 */

public class ClientHandler extends Thread {
	
	//@ invariant getChat() == true || getChat() == false;
    //@ invariant getPlayerName() != null;
    //@ invariant getMarkInCurrentGame() == Mark.XX ||
	//@ getMarkInCurrentGame() == Mark.OO || getMarkInCurrentGame() == Mark.EMPTY;
    //@ invariant getReady() == true; getReady() == false;
	private Server server;
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	private String clientName;
	private boolean readyToStartGame;
	private ServerGame game;
	private Mark mark;
	private int move;
	private TUIServer sUI;
	private boolean chat;
	/**
	 * Constructs a ClientHandler object
	 * Initialises both Data streams.
	 * @param serverArg the server
	 * @param sockArg the socket
	 * @param ui the view
	 */
    //@ requires serverArg != null && sockArg != null && ui != null;
    //@ ensures server == serverArg;
	//@ ensures sock == sockArg;
	//@ ensures sUI == ui;
	//@ ensures in == BufferedReader();
	//@ ensures out == BufferedWriter();
	//@ ensures readyToStartGame == false;
	//@ ensures game == null;
	//@ ensures mark == Mark.EMPTY;
	//@ ensures move == -1;
	//@ ensures chat == false;
	public ClientHandler(Server serverArg, Socket sockArg, TUIServer ui) throws IOException {
		this.server = serverArg;
		this.sock = sockArg;
		this.sUI = ui;
		this.in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
    	this.out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
    	readyToStartGame = false;
    	game = null;
    	mark = Mark.EMPTY;
    	move = -1;
    	chat = false;
	}
	/**
     * @return chat
     */
    //@ ensures \result == chat;
	/*@ pure */ public boolean getChat() {
		return chat;
	}
	
	/**
     * @return clientName
     */
    //@ ensures \result == clientName;
	/*@ pure */ public String getPlayerName() {
		return clientName;
	}
	
	/**
     * @return mark
     */
    //@ ensures \result == mark;
	/*@ pure */ public Mark getMarkInCurrentGame() {
		return mark;
	}
	
	/**
     * sends a message that the game ended in a draw.
     */
    //@ ensures sendMessage("game_end");
	public void broadcastDraw() {
		sendMessage("game_end");
	}
	
	/**
	 * sends a message that the game has ended and a player has won.
     * @param winner the player that has won.
     */
    //@ ensures sendMessage("game_end " + winner.getPlayerName());
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
		//@ loop_invariant running == true || running == false;
		//@ loop_invariant \result == (running == false);
		while (running) {
			try {
				String inputString = in.readLine();
				String[] input = inputString.split(" ");
				sUI.message(inputString);
				if (input[0].equals("join") && input.length >= 3) {
					clientName = input[1];
					if (inputString.contains(" Chat")) {
						chat = true;
					}
					int playersWithSameName = 0;
					for (ClientHandler c: server.getClients()) {
						if (c.getPlayerName().equals(clientName)) {
							playersWithSameName++;
						}
					}
					if (playersWithSameName == 1) {
						sendMessage("accept 12 Chat");
					} else {
						sendMessage("error 004");
					}
				} else if (input[0].equals("ready_for_game") && input.length == 1) {
					readyToStartGame = true;
					server.tryToStartGame(this);
				} else if (input[0].equals("do_move")) {
					move = Integer.parseInt(input[1]);
				} else if (input[0].equals("chat_global")) {
					String[] message = inputString.split(" ", 2);
					server.broadcastMesGlobal(message[1], clientName);
				} else if (input[0].equals("chat_local")) {
					if (game != null) {
						String[] message = inputString.split(" ", 2);
						server.broadcastToGame(game, message[1], clientName);
					}
				} else if (input[0].equals("error")) {
					sUI.message("error" + input);
				} else {
					sendMessage("error 007");
					sUI.message(inputString);
				}
			} catch (IOException e) {
				shutdown();
				break;
			}
		}
	}
	
	/**
	 * sends a message when a move cannot be done.
     */
    //@ ensures sendMessage("error " + "002");
	public void broadcastMoveCannotBeDone() {
		sendMessage("error " + "002");
	}
	
	/**
	 * sends a message when a move needs to be made by a player.
     * @param c the player that must make a move
     */
    //@ ensures sendMessage("request_move " + c.getPlayerName());
	public void requestMove(ClientHandler c) {
		sendMessage("request_move " + c.getPlayerName());
	}
	
	/**
	 * sends a message when a move has been made by a player.
     * @param player the player that must make a move
     * @param moves the move that has been made
     */
    //@ ensures sendMessage("done_move " + player.getPlayerName() + " " + moves);
	public void broadCastMove(ClientHandler player, int moves) {
		sendMessage("done_move " + player.getPlayerName() + " " + moves);
	}
	
	/**
	 * waits for a move by this player.
	 * @return int returns the move
     */
    //@ ensures move == -1;
	public int getMove() {
		int result = -1;
		//@ loop_invariant move == -1 || move >=0;
		while (move == -1) {
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
	
	/**
	 * put this client in game state.
	 * @param g the game
	 * @param m the mark for this player
     */
    //@ ensures game == g;
	//@ ensures mark == m;
	public void startGame(ServerGame g, Mark m) {
		this.game = g;
		this.mark = m;
	}
	
	/**
	 * see if this client is ready for a game.
	 * @return boolean true if the client is ready
     */
    //@ ensures \result == readyToStartGame;
	/*@ pure */ public boolean getReady() {
		return readyToStartGame;
	}
	
	/**
	 * set the client's readiness for a game.
	 * @param r what the ready state has to be set to
     */
    //@ ensures readyToStartGame == r;
	public void setReady(boolean r) {
		this.readyToStartGame = r;
	}

	/**
	 * This method can be used to send a message over the socket
     * connection to the Client. If the writing of a message fails,
     * the method concludes that the socket connection has been lost
     * and shutdown() is called.
     * @param msg what the message needs to be
	 */
	//@ ensures (out.write(msg + "\n")&& out.flush()) || shutdown();
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
	//@ ensures game != null ==> server.leftGame(game, this);
	//@ ensures server.removeHandler(this);
	private void shutdown() {
		if (game != null) {
			server.leftGame(game, this);
		}
		server.removeHandler(this);
	}

}