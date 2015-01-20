package players;
import game.Board;
import game.Mark;
import views.View;


public abstract class Player {


    private String name;
    private Mark mark;

    public Player(String theName, Mark theMark) {
        this.name = theName;
        this.mark = theMark;
    }

    public String getName() {
        return name;
    }

    public Mark getMark() {
        return mark;
    }

    public abstract int determineMove(Board board, View v);

    public boolean makeMove(Board board, View v) {
        int keuze = determineMove(board, v);
        return board.makeMove(keuze, getMark());
    }
}
