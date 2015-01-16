

public class ComputerPlayer extends Player{

	private String name;
	private Mark mark;
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
	
	public int determineMove(Board board) {
		return strategy.determineMove(board, mark);
	}
}
