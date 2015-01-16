import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }
    
    public int determineMove(Board board, View v) {
        return v.getHumanMove();
    }
}
