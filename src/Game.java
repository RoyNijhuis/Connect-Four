
import java.util.Observable;
import java.util.Scanner;

public class Game extends Observable{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private int current;
    private View UI;
    
    public Game(Player s0, Player s1, View v) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
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
        		players[i].makeMove(board, UI);
        		this.setChanged();
                this.notifyObservers("printBoard");
                
        		if(board.gameOver())
        		{
        			gameOver = true;
        			//notify observers
        			break;
        		}
        	}
        }
    }
}
