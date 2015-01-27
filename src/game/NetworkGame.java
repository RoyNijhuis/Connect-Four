package game;

import players.ComputerPlayer;
import players.HumanPlayer;
import players.Player;
import views.View;

import java.util.Observable;

public class NetworkGame extends Observable implements Game{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player self;
    private Player other;
    private Player current;
    private View UI;
    
    public NetworkGame(Player s0, Player s1, View v) {
        board = new Board();
        if(s0 instanceof HumanPlayer || s0 instanceof ComputerPlayer) {
        	self = s0;
        	other = s1;
        } else {
        	self = s1;
        	other = s0;
        }
        UI = v;
    	this.addObserver(UI);
    }
    
    public Board getBoard(){
    	return board;
    }
    
    public int askForMove() {
    	int move=-1;
    	if(self instanceof HumanPlayer) {
    		move = UI.getHumanMove(self.getName());
    	} else if(self instanceof ComputerPlayer) {
    		move = self.determineMove(board, UI);
    	}
    	return move;
    }
    
    public void moveDone(String name, int move) {
    	if(self.getName().equals(name)) {
    		board.makeMove(move, self.getMark());
    		current = self;
    	} else if(other.getName().equals(name)) {
    		board.makeMove(move, other.getMark());
    		current = other;
    	}
    	//notify observers
    	this.setChanged();
        this.notifyObservers("printBoard");
    }
    
    private void play() {
        this.setChanged();
        this.notifyObservers("printBoard");
    }
    
    public Player getWinner() {
    	return current;
    }
    
    public void gameOver() {
    	this.setChanged();
    	this.notifyObservers("gameOver");
    }
}
