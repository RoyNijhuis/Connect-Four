package strategies;
import game.Board;
import game.Mark;



public interface Strategy {
	
	public String getName();
	public int determineMove(Board b, Mark m);
}
