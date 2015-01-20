
public class Board {


    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;

    private Mark[][] fields;
    private int lastMoveCol = -1;
    private int lastMoveRow = -1;
    private Mark lastMark = null;
    
    public Board() {
    	fields = new Mark[HEIGHT][WIDTH];
    	for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
        		fields[i][j] = Mark.EMPTY;
        	}
    	}
    }
    
    public Mark[][] getField(){
    	return fields;
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

    public boolean gameOver() {
        return this.isWinner(lastMark, lastMoveCol, lastMoveRow) || this.isFull();
    }
    
    public void makeMove(int move, Mark m){ //moet nog error als zet niet kan
    	if(fields[0][move].equals(Mark.EMPTY)){
    		boolean madeMove = false;
    		int row = HEIGHT-1;
    		while(!madeMove){
    			if(fields[row][move].equals(Mark.EMPTY)){
    				fields[row][move] = m;
    				lastMark = m;
    				lastMoveRow = row;
    				lastMoveCol = move;
    				madeMove = true;
    				
    			} else {
    				row --;
    			}
    		}
    	}
    }
    
    public boolean hasRow(Mark m, int width, int height) {
    	boolean result = false;
    	int counter=0;
    	int startWidth=width-3;
    	if(startWidth < 0) {
    		startWidth=0;
    	}
    	
	    for(int i=0;i<7;i++) {
	    	if(startWidth+i>=WIDTH) {
	    		break;
	    	}
	    	if(fields[height][startWidth+i].equals(m)) {
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
    	int counter = 3;
		int counted = 1;
		
		if(height > 2){
			win = false;
		} else {
			while(!win) {
				if(fields[height+counted][width].equals(m)){
					win = counted>=counter;
					counted ++;
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
		while(!win && (height + counted)<HEIGHT && (width+counted)<WIDTH ) {
			if(fields[height+counted][width+counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				counted = 1;
				break;
			}
		}
		if(win){
			System.out.println("1" + m);
		}
		while(!win && (height - counted)>=0 && (width-counted)>=0 ) {
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
		if(win){
			System.out.println("2" + m);
		}
		while(!win && (height - counted)>= 0 && (width+counted)<WIDTH ) {
			if(fields[height-counted][width+counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				counted = 1;
				break;
			}
		}
		if(win){
			System.out.println("3" + m);
		}
		while(!win && (height + counted)<HEIGHT && (width-counted)>=0 ) {
			if(fields[height+counted][width-counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				break;
			}
		}
		if(win){
			System.out.println("4" + m);
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
