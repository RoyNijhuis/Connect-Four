package players;
import game.Board;
import game.Mark;
import views.View;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }
    
    public int determineMove(Board board, View v) {
        return v.getHumanMove(getName());
    }
}
