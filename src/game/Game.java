package game;

import java.util.Observable;
import java.util.Scanner;

import players.Player;
import views.View;

public class Game extends Observable{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private Player current;
    private View UI;
    
    public Game(Player s0, Player s1, View v) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        UI = v;
    	this.addObserver(UI);
        play();
    }
    
    public Board getBoard(){
    	return board;
    }
    
    private void play() {
        this.setChanged();
        this.notifyObservers("printBoard");
        boolean gameOver = false;
        while(!gameOver)
        {
        	for(int i=0;i<players.length;i++)
        	{
        		current = players[i];
        		if(players[i].makeMove(board, UI)) {
        			this.setChanged();
                    this.notifyObservers("printBoard");
                    
            		if(board.gameOver())
            		{
            			gameOver = true;
            			this.setChanged();
                        this.notifyObservers("gameOver");
            			break;
            		}
        		} else {
        			this.setChanged();
                    this.notifyObservers("columnFull");
        			i--;
        		}
        	}
        }
    }
    
    public Player getWinner() {
    	return current;
    }
}
