import java.util.List;
import java.util.Map;
import java.util.Observer;
import java.util.Scanner;


public abstract interface View extends Observer {
	public int getHumanMove();
	public Player[] askForPlayers();
	
	public static String askWhichUI() {
		boolean choiceMade = false;
		String result = null;
		
		while(!choiceMade) {
			System.out.println("Which UI would you like to use?");
			System.out.println("Type in: 'GUI' for a graphical user interface or 'TUI' for a textual user interface.");
			Scanner s = new Scanner(System.in);
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
