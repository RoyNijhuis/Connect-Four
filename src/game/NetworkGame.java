package game;

import players.ComputerPlayer;
import players.HumanPlayer;
import players.Player;
import views.View;

import java.util.Observable;

public class NetworkGame extends Observable implements Game {

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player self;
    private Player other;
    private Player current;
    private View uIS;
    
    /**
     * @param s0 speler1
     * @param s1 speler2
     * @param v de UI
     * Deze functie initialiseerd een NetworkGame met spelers.
     */
    public NetworkGame(Player s0, Player s1, View v) {
        board = new Board();
        if (s0 instanceof HumanPlayer || s0 instanceof ComputerPlayer) {
        	self = s0;
        	other = s1;
        } else {
        	self = s1;
        	other = s0;
        }
        uIS = v;
    	this.addObserver(uIS);
    }
    
    /**
     * Deze functie geeft het board terug.
     * @return Board
     */
    public Board getBoard() {
    	return board;
    }
    
    /**
     * Deze functie vraagt om een move.
     * @return int geeft een gekozen naam terug
     */
    public int askForMove() {
    	int move = -1;
    	if (self instanceof HumanPlayer) {
    		move = uIS.getHumanMove(self.getName());
    	} else if (self instanceof ComputerPlayer) {
    		move = self.determineMove(board, uIS);
    	}
    	return move;
    }
    
    /**
     * @param name de naam van de speler
     * @param move de zet die gedaan is
     * Deze functie verwerkt een zet.
     */
    public void moveDone(String name, int move) {
    	if (self.getName().equals(name)) {
    		board.makeMove(move, self.getMark());
    		current = self;
    	} else if (other.getName().equals(name)) {
    		board.makeMove(move, other.getMark());
    		current = other;
    	}
    	this.setChanged();
        this.notifyObservers("printBoard");
    }
        
    /**
     * @return Player geeft de winnaar van het spel terug
     */
    public Player getWinner() {
    	return current;
    }
    
    /**
     * @param winner naam van de winnaar
     * Deze functie zegt tegen de UI dat de game voorbij is en vraagt 
     * of er nog een keer gespeeld moet worden.
     */
    public void gameOver(String winner) {
    	this.setChanged();
    	this.notifyObservers("gameOver " + winner);
    	this.setChanged();
        this.notifyObservers("askPlayAgain");
    }
}
