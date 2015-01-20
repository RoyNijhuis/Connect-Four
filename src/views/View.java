package views;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import players.Player;


public abstract interface View extends Observer {
	public int getHumanMove(String name);
	public Player[] askForPlayers();
	public void printBoard(Observable o);
	public void gameOver(Observable o);
	
	public static String askWhichUI() {
		boolean choiceMade = false;
		String result = null;
		Scanner s = new Scanner(System.in);
		
		while(!choiceMade) {
			System.out.println("Which UI would you like to use?");
			System.out.println("Type in: 'GUI' for a graphical user interface or 'TUI' for a textual user interface.");
			String choice = s.nextLine();
			
			switch(choice) {
			case "TUI":
				result = "TUI";
				choiceMade = true;
				break;
			case "GUI":
				result = "GUI";
				choiceMade = true;
				break;
			default:
				System.out.println("Please enter a valid command!");
				break;
			}
		}
		return result;
	}
}
