

/**
 * Game student for the Tic Tac Toe game. Module 2 lab assignment.
 * 
 * @author Theo Ruys en Arend Rensink
 * @version $Revision: 1.4 $
 */
public class Board {

    // -- Constants --------------------------------------------------

    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    private static final String[] NUMBERING = {" 0 | 1 | 2 ", "---+---+---",
        " 3 | 4 | 5 ", "---+---+---", " 6 | 7 | 8 "};
    private static final String LINE = NUMBERING[1];
    private static final String DELIM = "     ";

    // -- Instance variables -----------------------------------------

    /*@
       private invariant fields.length == DIM*DIM;
       invariant (\forall int i; 0 <= i & i < DIM*DIM;
           getField(i) == Mark.EMPTY || getField(i) == Mark.XX || getField(i) == Mark.OO);
     */
    /**
     * The DIM by DIM fields of the Tic Tac Toe student. See NUMBERING for the
     * coding of the fields.
     */
    private Mark[][] fields;

    // -- Constructors -----------------------------------------------

    /*@
       ensures (\forall int i; 0 <= i & i < DIM * DIM; this.getField(i) == Mark.EMPTY);
     */
    /**
     * Creates an empty student.
     */
    public Board() {
    	fields = new Mark[HEIGHT][WIDTH];
    	for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
        		fields[i][j] = Mark.EMPTY;
        	}
    	}
    }

    // -- Queries ----------------------------------------------------

    /*@
       ensures \result != this;
       ensures (\forall int i; 0 <= i & i < DIM * DIM; \result.getField(i) == this.getField(i));
     */
    /**
     * Creates a deep copy of this field.
     */
    public Board deepCopy() {
    	Board result = new Board();
        for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
    			result.fields[i][j] = this.fields[i][j];
        	}
    	}
        return result;
    }

    /*@
       ensures \result == (\forall int i; i <= 0 & i < DIM * DIM; this.getField(i) != Mark.EMPTY);
     */
    /**
     * Tests if the whole student is full.
     * 
     * @return true if all fields are occupied
     */
    /*@pure*/
    public boolean isFull() {
        boolean result = true;
        for(int i=0;i<fields.length;i++)
        {
        	if(this.isEmptyField(i))
        	{
        		result = false;
        	}
        }
        if(result)
        {
        	System.out.println("FULL");
        }
        return result;
    }

    /*@
       ensures \result == this.isFull() || this.hasWinner();

     */
    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the whole student is full.
     * 
     * @return true if the game is over
     */
    /*@pure*/
    public boolean gameOver() {
        return this.hasWinner() || this.isFull();
    }

    /**
     * Checks whether there is a row which is full and only contains the mark
     * <code>m</code>.
     * 
     * @param m
     *            the mark of interest
     * @return true if there is a row controlled by <code>m</code>
     */
    public boolean hasRow(Mark m) {
        for(int i=0;i<DIM;i++)
        {
        	boolean winning = true;
        	for(int j=0;j<DIM;j++)
            {
            	if(getField(j,i) != m)
            	{
            		winning = false;
            	}
            }
        	if(winning == true)
        	{
        		System.out.println("HASROW");
        		return true;
        	}
        }
        return false;
    }

    /**
     * Checks whether there is a column which is full and only contains the mark
     * <code>m</code>.
     * 
     * @param m
     *            the mark of interest
     * @return true if there is a column controlled by <code>m</code>
     */
    public boolean hasColumn(Mark m) {
    	for(int i=0;i<DIM;i++)
        {
        	boolean winning = true;
        	for(int j=0;j<DIM;j++)
            {
            	if(getField(i,j) != m)
            	{
            		winning = false;
            	}
            }
        	if(winning == true)
        	{
        		System.out.println("HASCOLUMN");
        		return true;
        	}
        }
        return false;
    }

    /**
     * Checks whether there is a diagonal which is full and only contains the
     * mark <code>m</code>.
     * 
     * @param m
     *            the mark of interest
     * @return true if there is a diagonal controlled by <code>m</code>
     */
    public boolean hasDiagonal(Mark m) {
    	boolean winning = true;
        for(int i=0;i<DIM;i++) {
        	if(this.getField(i,i) != m)
        	{
        		winning = false;
        	}
        }
        if(winning)
        {
        	System.out.println("HASDIAG1");
        	return winning;
        }
        
        winning = true;
        for(int i=0;i<DIM;i++) {
        	if(this.getField(DIM-1-i,i) != m)
        	{
        		winning = false;
        	}
        }
        if(winning)
        {
        	System.out.println("HASDIAG2");
        	return winning;
        }
        return false;
    }

    /*@
       requires m == Mark.XX | m == Mark.OO;
       ensures \result == this.hasRow(m) ||
                                this.hasColumn(m) |
                                this.hasDiagonal(m);
     */
    /**
     * Checks if the mark <code>m</code> has won. A mark wins if it controls at
     * least one row, column or diagonal.
     * 
     * @param m
     *            the mark of interest
     * @return true if the mark has won
     */
    /*@pure*/
    public boolean isWinner(Mark m) {
        return this.hasRow(m) ||
                this.hasColumn(m) ||
                this.hasDiagonal(m);
    }

    /*@
       ensures \result == isWinner(Mark.XX) | \result == isWinner(Mark.OO);

     */
    /**
     * Returns true if the game has a winner. This is the case when one of the
     * marks controls at least one row, column or diagonal.
     * 
     * @return true if the student has a winner.
     */
    /*@pure*/
    public boolean hasWinner() {
    	return isWinner(Mark.XX) || isWinner(Mark.OO);
    }

    /**
     * Returns a String representation of this student. In addition to the current
     * situation, the String also shows the numbering of the fields.
     * 
     * @return the game situation as String
     */
    public String toString() {
    	//TODO
    	return null;
    }
}
