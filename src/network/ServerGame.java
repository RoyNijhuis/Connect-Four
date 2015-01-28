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
    
    
    public ServerGame(ClientHandler c1, ClientHandler c2) {
        board = new Board();
        clients = new ClientHandler[2];
        clients[0] = c1;
        clients[1] = c2;
        winner = null;
    }
    
    /*@ pure */ public ClientHandler[] getClients() {
    	return clients;
    }
    
    /*@ pure */ public Board getBoard() {
    	return board;
    }
    
    public void play() {
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
    
    private void requestMove(ClientHandler c) {
    	for (int i = 0; i < NUMBER_PLAYERS; i++) {
    		clients[i].requestMove(c);
    	}
    }
    
    private void broadcastMove(ClientHandler player, int move) {
    	
    	for (int i = 0; i < NUMBER_PLAYERS; i++) {
    		clients[i].broadCastMove(player, move);
    	}
    }
    
    private int waitForMove() {
    	return current.getMove();
    }

	@Override
	public Player getWinner() {
		return null;
	}

}
