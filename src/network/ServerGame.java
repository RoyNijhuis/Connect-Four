package network;

import game.Board;
import game.Game;
import players.Player;

/**
 * Board. 
 * @author  Roy & Edwin
 * @version 2015.01.28
 */

public class ServerGame extends Thread implements Game {

	public static final int NUMBER_PLAYERS = 2;
	
	//@ invariant getClients() != null;
	//@ invariant getBoard() != null;
	
    private Board board;
    private boolean gameDone = false;
    private ClientHandler[] clients;
    private ClientHandler current;
    private ClientHandler winner;
    boolean terminated = false;
    
    /**
     * play's the game until the game is done.
     * then it sends a message what the final game state was if it wasn't terminated
     */
    //@ ensures gameDone;
    public void run() {
    	//@ loop_invariant gameDone == true || gameDone == false;
    	while (!gameDone) {
    		play();
    	}
    	
    	if (winner == null && !terminated) {
    		for (ClientHandler c: clients) {
    			c.broadcastDraw();
    		}
    	} else if (!terminated) {
    		for (ClientHandler c: clients) {
    			c.broadcastWinner(winner);
    		}
    	}
    }
    
    /**
     * terminates the game and sets the gameDone to true.
     */
    //@ ensures gameDone == true;
    //@ ensures terminated == true;
    public void terminate() {
    	terminated = true;
    	gameDone = true;
    }
    
    /**
     * creates a new ServerGame with the 2 players.
     * @param c1 the first client
     * @param c2 the second client
     */
    //@ requires c1 != null
    //@ requires c2 != null
    //@ ensures board != null;
    //@ ensures clients != null;
    //@ ensures winner == null;
    public ServerGame(ClientHandler c1, ClientHandler c2) {
        board = new Board();
        clients = new ClientHandler[2];
        clients[0] = c1;
        clients[1] = c2;
        winner = null;
    }
    
    /**
     * @return ClientHandler[] returns the clients
     */
    //@ ensures \result == clients;
    /*@ pure */ public ClientHandler[] getClients() {
    	return clients;
    }
    
    /**
     * @return board returns the board
     */
    //@ ensures \result == board;
    /*@ pure */ public Board getBoard() {
    	return board;
    }
    /**
     * The game plays and lets the player make a move.
     * 
     */
    //@ ensures moveMade == true;
    public void play() {
    	//@ loop_invariant 0 <= i && i < NUMBER_PLAYERS;
    	//@ loop_invariant board.isWinner(board.lastMark(), board.lastWidth(), board.lastHeight()
    	//@ == true ==> (gameDone == true);
    	for (int i = 0; i < NUMBER_PLAYERS; i++) {
    		boolean moveMade = false;
    		current = clients[i];
    		while (!moveMade) {
        		requestMove(current);
        		int moveDone = waitForMove();
        		
        		if (board.makeMove(moveDone, current.getMarkInCurrentGame())) {
        			broadcastMove(current, moveDone);
        			moveMade = true;
        			break;
        		} else {
        			current.broadcastMoveCannotBeDone();
        		}
    		}
    		
    		if (!board.isWinner(board.lastMark(), board.lastWidth(), board.lastHeight())) {
    			if (board.isFull()) {
    				gameDone = true;
        			winner = null;
        			break;
    			}
    		} else {
    			gameDone = true;
    			winner = current;
    			break;
    		}
    	}
    }
    
    /**
     * request a move from the client.
     * @param c client that has to make a move
     */
    //@ ensures \forAll int i; 0 <= i && i < NUMBER_PLAYERS; clients[i].requestMove(c);
    private void requestMove(ClientHandler c) {
    	//@ loop_invariant \forAll int y; 0 <= i && y < i; clients[y].requestMove(c);
    	for (int i = 0; i < NUMBER_PLAYERS; i++) {
    		clients[i].requestMove(c);
    	}
    }
    
    /**
     * broadcast a move to all clients.
     * @param player client that has made a move
     * @param move the move
     */
    //@ ensures \forAll int i; 0 <= i && i < NUMBER_PLAYERS; clients[i].broadCastMove(player, move);
    private void broadcastMove(ClientHandler player, int move) {
    	//@ loop_invariant \forAll int y; 0 <= i && y < i;  clients[y].broadCastMove(player, move);
    	for (int i = 0; i < NUMBER_PLAYERS; i++) {
    		clients[i].broadCastMove(player, move);
    	}
    }
    
    /**
     * ask for a move.
     * @return int the move from the client
     */
    //ensures \result == current.getMove();
    private int waitForMove() {
    	return current.getMove();
    }

	@Override
	public Player getWinner() {
		return null;
	}

}
