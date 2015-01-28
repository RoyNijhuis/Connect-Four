package players;

import game.Board;
import game.Mark;
import views.View;

public class NetworkPlayer extends Player {

	public NetworkPlayer(String theName, Mark theMark) {
		super(theName, theMark);
	}

	public int determineMove(Board board, View v) {
		
		return 0;
	}
}
