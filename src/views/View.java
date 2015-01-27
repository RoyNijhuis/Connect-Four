package views;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

import network.Client;
import players.Player;


public abstract interface View extends Observer {
	public int getHumanMove(String name);
	public Player[] askForPlayers();
	public String askLocalOrOnline();
	public String askPlayerName();
	public String askIPAdress();
	public String askPort();
	public void setClient(Client client);
	public void giveHint(int i);
	
	public static String askWhichUI() {
		boolean choiceMade = false;
		String result = null;
		
		while(!choiceMade) {
			System.out.println("Which UI would you like to use?");
			System.out.println("Type in: 'GUI' for a graphical user interface or 'TUI' for a textual user interface.");
			String choice = readString("");
			
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
