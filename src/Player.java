
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

    public void makeMove(Board board, View v) {
        int keuze = determineMove(board, v);
        board.makeMove(keuze, getMark());
    }

}
