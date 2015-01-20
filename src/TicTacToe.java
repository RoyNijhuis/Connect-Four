import java.util.Observable;

public class TicTacToe extends Observable{
	
	public TicTacToe() {
		View v;
		String ui = View.askWhichUI();
		switch(ui) {
		case "TUI":
			v = new TUI();
			break;
		case "GUI":
			v = new GUI();
			break;
		default:
			v = null;
		}
		this.addObserver(v);
		Player players[] = v.askForPlayers();
		this.setChanged();
		this.notifyObservers("playersAsked");
		this.createNewGame(players, v);
	}
	
	public static void main(String args[])
	{
		new TicTacToe();
	}
	
	public void createNewGame(Player[] players, View v) {
		Game game = new Game(players[0], players[1], v);
	}
}
