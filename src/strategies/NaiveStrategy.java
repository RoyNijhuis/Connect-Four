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
		for(int i=0;i<Board.DIM*Board.DIM;i++)
		{
			if(b.getField(i) == Mark.EMPTY)
			{
				emptySpaces.add(i);
			}
		}
		return emptySpaces.get(((int)(Math.random()*emptySpaces.size())));
	}
}
