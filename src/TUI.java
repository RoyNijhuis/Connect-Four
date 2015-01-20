import java.util.Observable;
import java.util.Scanner;


public class TUI implements View{

	private Scanner s;
	
	public TUI() {
		s = new Scanner(System.in);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			printBoard(o);
		} else if(arg.equals("gameOver")) {
			gameOver(o);
			Board board = ((Game) o).getBoard();
			Mark[][] field = board.getField();
			for(int i=board.HEIGHT-1;i>=0;i--) {
	    		for(int j=0;j<board.WIDTH;j++) {
	        		System.out.print(" \t|" + field[i][j] + " \t|");
	        	}
	    		System.out.println();
	    	}
		}
	}

	@Override
	public int getHumanMove(String name) {
		boolean done = false;
		int move=0;
		
		while(!done) {
			System.out.println(name + ", please enter your move(a digit between 1-7");
			try {
				move = Integer.parseInt(s.nextLine());
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
				System.out.println("Please enter the Type('H' for HumanPlayer) and the Name of Player " + i + " seperated witch a space: ");
				String input = s.nextLine();
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
		for(int i=board.HEIGHT-1;i>=0;i--) {
    		for(int j=0;j<board.WIDTH;j++) {
        		System.out.print(" \t|" + field[i][j] + " \t|");
        	}
    		System.out.println();
    	}//Mark[][] field = Board.getField();//DIT MOET NOG WORDEN TOEGEVOEGD!!!
	}

	@Override
	public void gameOver(Observable o) {
		System.out.println(((Game)o).getWinner() + " has won the game!");
	}
}
