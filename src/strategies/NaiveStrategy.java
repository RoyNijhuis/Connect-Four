package strategies;


import game.Board;
import game.Mark;

import java.util.ArrayList;

public class NaiveStrategy implements Strategy {

	public static final String NAME="naive";
	
	public String getName() {
		return NAME;
	}

	public int determineMove(Board b, Mark m) {
		ArrayList<Integer> emptySpaces = new ArrayList<Integer>();
		Mark[][] field = b.getField();
		for(int i=0;i<Board.WIDTH;i++)
		{
			if(field[0][i] == Mark.EMPTY)
			{
				emptySpaces.add(i);
			}
		}
		return emptySpaces.get(((int)(Math.random()*emptySpaces.size())));
	}
}
