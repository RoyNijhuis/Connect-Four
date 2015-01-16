
public class Board {


    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;

    private Mark[][] fields;
    
    public Board() {
    	fields = new Mark[HEIGHT][WIDTH];
    	for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
        		fields[i][j] = Mark.EMPTY;
        	}
    	}
    }
    
    public Board deepCopy() {
    	Board result = new Board();
        for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
    			result.fields[i][j] = this.fields[i][j];
        	}
    	}
        return result;
    }
    
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

    public boolean gameOver() {
        return this.hasWinner() || this.isFull();
    }

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

    public boolean isWinner(Mark m) {
        return this.hasRow(m) ||
                this.hasColumn(m) ||
                this.hasDiagonal(m);
    }

    public boolean hasWinner() {
    	return isWinner(Mark.XX) || isWinner(Mark.OO);
    }

    public String toString() {
    	return null;
    }
}
