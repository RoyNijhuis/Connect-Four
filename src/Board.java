
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
        for(int i=0;i<WIDTH;i++) {
        	if(fields[0][i].equals(Mark.EMPTY)) {
        		result=false;
        	}
        }
        return result;
    }

    public boolean gameOver(int width, int height) {
        return this.hasWinner(width, height) || this.isFull();
    }

    public boolean hasRow(Mark m, int width, int height) {
    	boolean result = false;
    	int counter=0;
    	int startWidth=width-3;
    	if(startWidth < 0) {
    		startWidth=0;
    	}
    	
	    for(int i=0;i<7;i++) {
	    	if(counter>=WIDTH) {
	    		break;
	    	}
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
		
		if(height < 3){
			win = false;
		} else {
			while(!win) {
				if(fields[height+counted][width].equals(m)){
					counted += 1;
					win = counted==counter;
				} else {
					break;
				}
			}
		}
        return win;
    }

    public boolean hasDiagonal(Mark m, int width, int height) {
    	boolean win = false;
    	int counter = 4;
		int counted = 1;
		int steps = 1;
		while(!win && (height + counted)<=HEIGHT && (width+counted)<=WIDTH ) {
			if(fields[height+counted][width+counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				counted = 1;
				break;
			}
		}
		while(!win && (height - counted)<=0 && (width-counted)<=0 ) {
			if(fields[height-counted][width-counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				counted = 1;
				steps = 1;
				break;
			}
		}
		while(!win && (height - counted)<=HEIGHT && (width+counted)<=WIDTH ) {
			if(fields[height-counted][width+counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				counted = 1;
				break;
			}
		}
		while(!win && (height + counted)<=0 && (width-counted)<=0 ) {
			if(fields[height+counted][width-counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				break;
			}
		}
        return win;
    }

    public boolean isWinner(Mark m, int width, int height) {
        return this.hasRow(m, width, height) ||
                this.hasColumn(m, width, height) ||
                this.hasDiagonal(m, width, height);
    }

    public boolean hasWinner(int width, int height) {
    	return isWinner(Mark.XX, width, height) || isWinner(Mark.OO, width, height);
    }

    public String toString() {
    	return null;
    }
}
