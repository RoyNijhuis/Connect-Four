package game;

import java.util.Observable;
import java.util.Scanner;

import players.Player;
import views.GUI;
import views.View;

public class NormalGame extends Observable implements Game{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private Player current;
    private View UI;
    
    public NormalGame(Player s0, Player s1, View v) {
        board = new Board();
        System.out.println("Game created!");
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
        	if(UI instanceof GUI) {
        		((GUI)UI).checkRetry();
        	}
        }
    }
    
    public Player getWinner() {
    	return current;
    }
}
