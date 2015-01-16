
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

    public boolean hasRow(Mark m, int width, int height) {
    	boolean result = false;
    	int counter=0;
    	int startWidth=width-3;
    	if(startWidth < 0) {
    		startWidth=0;
    	}
    	
	    for(int i=0;i<7;i++) {
	    	if(fields[height][counter].equals(m)) {
	    		counter++;
	    	} else {
	    		counter=0;
	    	}
	    	if(counter >= 4) {
	    		 result = true;
	    	}
	    }
	    return result;
    }

    public boolean hasColumn(Mark m, int width, int height) {
    	
    	boolean win = false;
    	int counter = 4;
		int counted = 1;
		int vSteps = 1;
		
		if(height < 3){
			win = false;
		} else {
			while(counted<counter) {
				if(fields[height-vSteps][width] == m){
					counted += 1;
					vSteps +=1;
					win = counted==counter;
				} else {
					break;
				}
			}
		}
        return win;
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
