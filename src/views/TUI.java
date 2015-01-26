package views;
import game.Board;
import game.ConnectFour;
import game.Game;
import game.NormalGame;
import game.Mark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Scanner;

import network.Client;
import players.HumanPlayer;
import players.Player;


public class TUI extends Thread implements View{
	private Client client;
	private BufferedReader in;
	
	public TUI(Client client){
		this.client = client;
		in = new BufferedReader(new InputStreamReader(System.in));
	}
	public void run() {
		while(true){
			try {
				String inputString = in.readLine();
				String[] input = inputString.split(" ");
				if(input[0].equals("say")) {
					String[] message = inputString.split(" ", 2);
					client.sendMessage("global_message " + message);
					System.out.println("said "+ message);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			printBoard(o);
		} else if(arg.equals("gameOver")) {
			gameOver(o);
		} else if(arg.equals("columnFull")) {
			System.out.println("This column is full...");
		} else if(arg.equals("badHost")) {
			badHost();
		} else if(arg.equals("cannotCreateClient")) {
			cannotCreateClient();
		} else if(arg.equals("cannotCreateClient")) {
			cannotCreateClient();
		} else if(arg.equals("askPlayAgain")) {
			askPlayAgain();
		}  else if(arg.equals("draw")) {
			draw();
		}
	}
	
	public void askPlayAgain() {
		System.out.println("Would you like to play again? (y/n)");
		String answer = readString("");
		while(!answer.equals("y") && !answer.equals("n")) {
			System.out.println("Please enter correct information.");
			System.out.println("Would you like to play again? (y/n)");
			answer = readString("");
		}
		if(answer.equals("y")) {
			new ConnectFour(this);
		} else {
			
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
	
	private void badHost() {
		System.out.println("Host is niet goed");
	}
	
	private void cannotCreateClient() {
		System.out.println("Kan Client niet aanmaken");
	}
	
	public String askLocalOrOnline() {
		System.out.println("Would you like to play a local game or an online game? (Type in: 'local' or 'online'");
		String answer = readString("");
		while(!answer.equals("local") && !answer.equals("online")) {
			System.out.println("Please enter correct information.");
			answer = readString("");
		}
		return answer;
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

	private void printBoard(Observable o) {
		Board board = ((Game) o).getBoard();
		Mark[][] field = board.getField();
		for(int i=0;i<Board.HEIGHT;i++) {
    		for(int j=0;j<Board.WIDTH;j++) {
        		System.out.print(" \t|" + field[i][j] + " \t|");
        	}
    		System.out.print("\n");
    	}//Mark[][] field = Board.getField();//DIT MOET NOG WORDEN TOEGEVOEGD!!!
	}

	private void gameOver(Observable o) {
		System.out.println(((Game)o).getWinner().getName() + " has won the game!");
	}
	
	private void draw() {
		System.out.println("The game ended in a draw...");
	}
	
	private String readString(String tekst) {
		System.out.print(tekst);
		String antw = null;
		try {
			
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}
}
