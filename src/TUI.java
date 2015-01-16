import java.util.Observable;
import java.util.Scanner;


public class TUI implements View{

	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			//DIT MOET NOG WORDEN TOEGEVOEGD!!!
		}
	}

	@Override
	public int getHumanMove() {
		// TODO Auto-generated method stub
		return 0;
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
				System.out.println("Please enter the Type('H' for HumanPlayer) and the Name of Player " + i + " seperated witch a space: ");
				Scanner s = new Scanner(System.in);
				String input = s.nextLine();
				String[] splitString = input.split(" ");
				if(splitString.length == 2 && splitString[0].equals("H")) {
					players[i-1] = new HumanPlayer(splitString[1], m);
					correct = true;
				} else {
					System.out.println("Please enter valid information...");
				}
			}
		}
		return players;
	}
}
