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
import players.*;
import strategies.SmartStrategy;


public class TUI extends Thread implements View{
	private Client client;
	private BufferedReader in;
	String expecting;
	String result;
	int resultMove;
	
	public TUI(){
		expecting = null;
		this.start();
		result = "";
		resultMove = 0;
		in = new BufferedReader(new InputStreamReader(System.in));
	}
	public void run() {
		while(true){
			try {
				String inputString = in.readLine();
				String[] input = inputString.split(" ");
				if(input[0].equals("say")) {
					String[] message = inputString.split(" ", 2);
					client.sendMessage("chat_global " + message[1]);
					System.out.println("said "+ message[1]);
				} else if(expecting.equals("name") && input.length == 1){
					result = inputString;
					expecting = "";
				} else if(expecting.equals("player1") && (input.length == 3||input.length == 2) && (input[0].equals("H")||input[0].equals("C"))){
					result = inputString;
					expecting = "";
				} else if(expecting.startsWith("player2") && (input.length == 3||input.length == 2) && (input[0].equals("H")||input[0].equals("C"))){
					String[] p1 = expecting.split(" ");
					if(!p1[1].equals(input[1])){
						result = inputString;
						expecting = "";
					} else {
						System.out.println("Please take a different name as player1");
					}
				} else if(expecting.equals("local")){
					if(inputString.equals("local")||inputString.equals("online")){
						result = inputString;
						expecting = "";
					} else {
						System.out.println("Please enter local or online");
					}
				} else if(expecting.equals("move")){
					try {
						int move = Integer.parseInt(inputString);
						if(move >= 1 && move <= 7) {
							resultMove = move;
							expecting = "";
						} else {
							System.out.println("Please enter a digit between 1 and 7");
						}
					} catch (NumberFormatException e) {
						System.out.println("Please enter a digit...");
					}
				} else if(expecting.equals("again")){
					if(inputString.equals("y")||inputString.equals("n")){
						result = inputString;
						expecting = "";
					} else {
						System.out.println("please enter correct information");
						System.out.println("Would you like to play again? (y/n)");
					}
				} else {
					System.out.println("please enter correct information");
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
		} else if(arg.equals("draw")) {
			draw();
		} else if(((String)arg).startsWith("accepted")) {
			String[] splitString = ((String) arg).split(" ");
			System.out.println("You are accepted!(group number: " + splitString[1] + ")");
		} else if(((String)arg).startsWith("nameExists")) {
			System.out.println("There already exists a player with this name on the server...");
		}
	}
	
	public void askPlayAgain() {
		System.out.println("Would you like to play again? (y/n)");
		expecting = "again";
		while(expecting.equals("again")){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if(result.equals("y")) {
			new ConnectFour(this);
		} else {
			
		}
		result = null;
	}

	@Override
	public int getHumanMove(String name) {
		System.out.println(name + ", please enter your move(a digit between 1-7)");
		expecting = "move";
		while(expecting.equals("move")){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int move = resultMove-1;
		resultMove = 0;
		return move;
	}
	
	private void badHost() {
		System.out.println("Host is niet goed");
	}
	
	private void cannotCreateClient() {
		System.out.println("Kan Client niet aanmaken");
	}
	
	public String askLocalOrOnline() {
		System.out.println("Would you like to play a local game or an online game? (Type in: 'local' or 'online')");
		expecting = "local";
		while(expecting.equals("local")){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String local = result;
		result = "";
		return local;
	}
	
	public String askPlayerName() {
		System.out.println("Please enter your name");
		expecting = "name";
		while(expecting.equals("name")){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String name = result;
		result = "";
		return name;
	}

	@Override
	public Player[] askForPlayers() {
		Player[] players = new Player[2];
		
		System.out.println("Please enter the Type('H' for HumanPlayer, 'C' for ComputerPlayer)\nfor example: 'H Henk' or 'C X'(X is a integer between 1-5)");
		expecting = "player1";
		while(expecting.equals("player1")){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String[] player = result.split(" ");
		if(player[0].equals("H")){
			players[0] = new HumanPlayer(player[1], Mark.XX);
		} else {
			players[0] = new ComputerPlayer(player[1], Mark.XX, new SmartStrategy());
		}
		String name = player[1];
		players[0] = new HumanPlayer(result, Mark.XX);
		expecting = "player2 "+name;
		System.out.println("Please enter the Type('H' for HumanPlayer, 'C' for ComputerPlayer)\nfor example: 'H Henk' or 'C Piet X'(X is a integer between 1-5)");
		
		while(expecting.equals("player2 "+name)){
			try {
				sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		player = result.split(" ");
		if(player[0].equals("H")){
			players[1] = new HumanPlayer(player[1], Mark.OO);
		} else {
			players[1] = new ComputerPlayer(player[1], Mark.OO, new SmartStrategy());
		}
		result = "";
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
	

	/*private String readString(String tekst) {
		System.out.print(tekst);
=======
	private String readString(String tekst) {
>>>>>>> origin/master
		String antw = null;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			antw = in.readLine();
		} catch (IOException e) {
		}

		return (antw == null) ? "" : antw;
	}*/
	
	@Override
	public void setClient(Client client) {
		this.client = client;
		
	}
}
