package strategies;


import game.Board;
import game.Mark;

import java.util.ArrayList;

public class SmartStrategy implements Strategy{

public static final String NAME="Smart";

	public String getName() {
		return NAME;
	}

	public int determineMove(Board b, Mark m) {
		Board copyOfBoard = b.deepCopy();
		
		for(int i=0;i<Board.DIM*Board.DIM;i++)
		{
			copyOfBoard.setField(i, m);
			if(copyOfBoard.hasWinner() && b.isEmptyField(i))
			{
				return i;
			}
			copyOfBoard = b.deepCopy();
		}
		
		Mark temp;
		if(m == Mark.OO)
		{
			temp = Mark.XX;
		}
		else
		{
			temp = Mark.OO;
		}
		
		for(int i=0;i<Board.DIM*Board.DIM;i++)
		{
			copyOfBoard.setField(i, temp);
			if(copyOfBoard.hasWinner() && b.isEmptyField(i))
			{
				return i;
			}
			copyOfBoard = b.deepCopy();
		}
		
		if(b.isEmptyField(4))
		{
			System.out.println("MIDDEN LEEG");
			return Board.DIM*Board.DIM/2;
		}
		
		ArrayList<Integer> emptySpaces = new ArrayList<Integer>();
		for(int i=0;i<Board.DIM*Board.DIM;i++)
		{
			if(b.getField(i).equals(Mark.EMPTY))
			{
				emptySpaces.add(i);
			}
		}
		copyOfBoard = b.deepCopy();
		return emptySpaces.get(((int)(Math.random()*emptySpaces.size())));
	}
}
