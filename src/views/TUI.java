package views;
import game.Board;
import game.Game;
import game.NormalGame;
import game.Mark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Scanner;

import players.HumanPlayer;
import players.Player;


public class TUI implements View{
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			printBoard(o);
		} else if(arg.equals("gameOver")) {
			gameOver(o);
			Board board = ((NormalGame) o).getBoard();
			Mark[][] field = board.getField();
			for(int i=0;i<Board.HEIGHT;i++) {
	    		for(int j=0;j<Board.WIDTH;j++) {
	        		System.out.print(" \t|" + field[i][j] + " \t|");
	        	}
	    		System.out.println();
	    	}
		} else if(arg.equals("columnFull")) {
			System.out.println("This column is full...");
		}
	}

	@Override
	public int getHumanMove(String name) {
		boolean done = false;
		int move=0;
		
		while(!done) {
			System.out.println(name + ", please enter your move(a digit between 1-7");
			try {
				move = Integer.parseInt(readString(""));
				if(move >= 1 && move <= 7) {
					done = true;
				} else {
					System.out.println("Please enter a digit between 1 and 7");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please enter a digit...");
			}
		}
		return move-1;
	}

	@Override
	public Player[] askForPlayers() {
		Player[] players = new Player[2];
		for(int i=1; i<=2;i++) {
			Mark m;
			if(i==1) {
				m = Mark.XX;
			} else {
				m = Mark.OO;
			}
			boolean correct = false;
			while(!correct) {
				System.out.println("Please enter the Type('H' for HumanPlayer, 'N' for NetworkPlayer), for example: 'H Henk' or 'N 1.2.3.4.5.6 2727'(ip and portnumber)");
				String input = readString("");
				String[] splitString = input.split(" ");
				if(splitString.length == 2 && splitString[0].equals("H")) {
					if(i==2 && players[0].getName().equals(splitString[1])) {
						System.out.println("Please enter a different name than player 1's name(" + players[0].getName() + ")");
					} else {
						players[i-1] = new HumanPlayer(splitString[1], m);
						correct = true;
					}
				} else {
					System.out.println("Please enter valid information...");
				}
			}
		}
		return players;
	}

	@Override
	public void printBoard(Observable o) {
		Board board = ((Game) o).getBoard();
		Mark[][] field = board.getField();
		for(int i=0;i<Board.HEIGHT;i++) {
    		for(int j=0;j<Board.WIDTH;j++) {
        		System.out.print(" \t|" + field[i][j] + " \t|");
        	}
    		System.out.print("\n");
    	}//Mark[][] field = Board.getField();//DIT MOET NOG WORDEN TOEGEVOEGD!!!
	}

	@Override
	public void gameOver(Observable o) {
		System.out.println(((NormalGame)o).getWinner().getName() + " has won the game!");
	}
	
	public static String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
}
