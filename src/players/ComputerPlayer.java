package players;
import game.Board;
import game.Mark;
//import strategies.NaiveStrategy;
import strategies.Strategy;
import views.View;



public class ComputerPlayer extends Player {

	//private String name;
	//private Mark mark;
	private Strategy strategy;
	
	public ComputerPlayer(String theName, Mark theMark) {
        super(theName, theMark);
    }
	
	public ComputerPlayer(String name, Mark mark, Strategy strategys) {
		this(name, mark);
		this.strategy = strategys;
	}
	
	@Override
	public int determineMove(Board board, View v) {
		return strategy.determineMove(board, this.getMark());
	}
}
