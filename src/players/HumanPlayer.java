package players;
import game.Board;
import game.Mark;
import views.View;
import strategies.SmartStrategy;

public class HumanPlayer extends Player {
	SmartStrategy hint;
    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
        hint = new SmartStrategy(3);
    }
    
    public int determineMove(Board board, View v) {
    	int i = hint.determineMove(board, this.getMark());
    	v.giveHint(i);
        return v.getHumanMove(getName());
    }
}
