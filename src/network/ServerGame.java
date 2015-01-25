package network;

import game.Board;
import game.Game;
import players.Player;
import views.View;

public class ServerGame extends Thread implements Game{

	public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private boolean gameDone = false;
    private ClientHandler[] clients;
    private ClientHandler current;
    private ClientHandler winner;
    
    public void run() {
    	while(!gameDone) {
    		play();
    	}
    	
    	//announce winner/draw
    	if(winner == null) {
    		for(ClientHandler c: clients) {
    			c.broadcastDraw();
    		}
    	} else {
    		for(ClientHandler c: clients) {
    			c.broadcastWinner(winner);
    		}
    	}
    }
    
    public ServerGame(ClientHandler c1, ClientHandler c2) {
        board = new Board();
        clients = new ClientHandler[2];
        clients[0] = c1;
        clients[1] = c2;
        winner = null;
    }
    
    public Board getBoard(){
    	return board;
    }
    
    public void play() {
    	for(int i=0;i<NUMBER_PLAYERS;i++)
    	{
    		current = clients[i];
    		requestMove(current);
    		int moveDone = waitForMove();
    		System.out.println("move received on server and broadcasted");
    		broadcastMove(current, moveDone);
    		
    		if(!board.isWinner(board.lastMark(), board.lastWidth(), board.lastHeight())) {
    			if(board.isFull()) {
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
    	for(int i=0;i<NUMBER_PLAYERS;i++)
    	{
    		clients[i].requestMove(c);
    	}
    }
    
    private void broadcastMove(ClientHandler player, int move) {
    	//register move on the server
    	board.makeMove(move, player.getMarkInCurrentGame());
    	
    	//send move to clients
    	for(int i=0;i<NUMBER_PLAYERS;i++)
    	{
    		clients[i].broadCastMove(player, move);
    	}
    }
    
    private int waitForMove() {
    	return current.getMove();
    }

	@Override
	public Player getWinner() {
		// TODO Auto-generated method stub
		return null;
	}

}
