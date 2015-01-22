package players;
import game.Board;
import game.Mark;
import strategies.NaiveStrategy;
import strategies.Strategy;
import views.View;



public class ComputerPlayer extends Player{

	//private String name;
	//private Mark mark;
	private Strategy strategy;
	
	public ComputerPlayer(String theName, Mark theMark) {
        super(theName, theMark);
    }
	
	public ComputerPlayer(Mark mark, Strategy strategy)
	{
		this(strategy.getName()+"-"+mark.toString(), mark);
		this.strategy = strategy;
	}
	
	public ComputerPlayer(Mark mark)
	{
		this(mark, new NaiveStrategy());
	}
	
	@Override
	public int determineMove(Board board, View v) {
		return strategy.determineMove(board, this.getMark());
	}
}
