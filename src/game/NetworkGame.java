package game;

import java.util.Observable;
import java.util.Scanner;

import players.HumanPlayer;
import players.Player;
import views.View;

public class NetworkGame extends Observable implements Game{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player self;
    private Player other;
    private Player current;
    private View UI;
    
    public NetworkGame(Player s0, Player s1, View v) {
        board = new Board();
        if(s0 instanceof HumanPlayer) {
        	self = s0;
        	other = s1;
        } else {
        	self = s1;
        	other = s0;
        }
        System.out.println("self: " + s0.getName());
        System.out.println("other: " + s1.getName());
        UI = v;
    	this.addObserver(UI);
    }
    
    public Board getBoard(){
    	return board;
    }
    
    public int askForMove() {
    	int move = UI.getHumanMove(self.getName());
    	return move;
    }
    
    public void moveDone(String name, int move) {
    	if(self.getName().equals(name)) {
    		board.makeMove(move, self.getMark());
    	} else if(other.getName().equals(name)) {
    		board.makeMove(move, other.getMark());
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
}