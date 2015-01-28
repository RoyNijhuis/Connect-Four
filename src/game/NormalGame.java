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
    
    /**
     * @param s0 speler 1
     * @param s1 speler 2
     * @param v de UI
     * Deze functie maakt een nieuwe NormalGame aan met spelers.
     */
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
    
    /**
     * @return Board geeft een Board terug
     * Deze functie maakt een nieuwe NormalGame aan met spelers.
     */
    public Board getBoard() {
    	return board;
    }
    
    /**
     * Deze functie blijft het spel spelen zolang de game niet afgelopen is.
     */
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
            				this.notifyObservers("gameOver " + current.getName());
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
    
    /**
     * @return Player geeft de winnaar terug
     * Deze functie maakt een nieuwe NormalGame aan met spelers.
     */
    public Player getWinner() {
    	return current;
    }
}
