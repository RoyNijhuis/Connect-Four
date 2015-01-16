

import java.util.Observable;
import java.util.Scanner;

public class Game extends Observable{

    public static final int NUMBER_PLAYERS = 2;

    private Board board;
    private Player[] players;
    private int current;

    public Game(Player s0, Player s1) {
        board = new Board();
        players = new Player[NUMBER_PLAYERS];
        players[0] = s0;
        players[1] = s1;
        current = 0;
        
        //Start the game loop
        
    }

    private void reset() {
        current = 0;
        board.reset();
    }

    private void play() {
        System.out.println(board);
        boolean gameOver = false;
        while(!gameOver)
        {
        	for(int i=0;i<players.length;i++)
        	{
        		players[i].makeMove(board);
        		update();
        		if(board.gameOver())
        		{
        			gameOver = true;
        			break;
        		}
        	}
        }
    }
}
