package game;

import java.util.Observable;

import players.Player;
import views.GUI;
import views.View;

public class NormalGame extends Observable implements Game {

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private Player current;
    private View uIS;
    
    public NormalGame(Player s0, Player s1, View v) {
        board = new Board();
        System.out.println("Game created!");
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        uIS = v;
    	this.addObserver(uIS);
        play();
    }
    
    public Board getBoard() {
    	return board;
    }
    
    private void play() {
        this.setChanged();
        this.notifyObservers("printBoard");
        boolean gameOver = false;
        while (!gameOver) {
        	for (int i = 0; i < players.length; i++) {
        		current = players[i];
        		if (players[i].makeMove(board, uIS)) {
        			this.setChanged();
                    this.notifyObservers("printBoard");
                    
            		if (board.gameOver()) {
            			gameOver = true;
            			this.setChanged();
            			if (board.isFull()) {
            				this.notifyObservers("draw");
            			} else {
            				this.notifyObservers("gameOver " + current);
            			}
                        this.setChanged();
                        this.notifyObservers("askPlayAgain");
            			break;
            		}
        		} else {
        			this.setChanged();
                    this.notifyObservers("columnFull");
        			i--;
        		}
        	}
        	if (uIS instanceof GUI) {
        		((GUI) uIS).checkRetry();
        	}
        }
        System.out.println("out of game loop");
    }
    
    public Player getWinner() {
    	return current;
    }
}
