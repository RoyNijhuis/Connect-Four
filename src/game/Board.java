package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Board {


    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;

    private Mark[][] fields;

    private int lastMoveCol = 0;
    private int lastMoveRow = 0;
    private Mark lastMark = Mark.EMPTY;
    
    public Board() {
    	fields = new Mark[HEIGHT][WIDTH];
    	for(int i=0;i<HEIGHT;i++) {
    		for(int j=0;j<WIDTH;j++) {
        		fields[i][j] = Mark.EMPTY;
        	}
    	}
    }
    
    public int lastHeight(){
    	return lastMoveRow;
    }
    
    public int lastWidth(){
    	return lastMoveCol;
    }
    
    public Mark lastMark(){
    	return lastMark;
    }
    
    public Mark[][] getField(){
    	return fields;
    }
    
    public Set<Integer> possibleMoves(){
    	Set<Integer> moves = new HashSet<Integer>();
    	for(int i=0;i<WIDTH;i++){
    		if(fields[0][i].equals(Mark.EMPTY)){
    			moves.add(i);
    		}
    	}
    	return moves;
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
    private static int[][] evaluationTable = {{3, 4, 5, 7, 5, 4, 3}, 
        {4, 6, 8, 10, 8, 6, 4},
        {5, 8, 11, 13, 11, 8, 5}, 
        {5, 8, 11, 13, 11, 8, 5},
        {4, 6, 8, 10, 8, 6, 4},
        {3, 4, 5, 7, 5, 4, 3}};

    //here is where the evaluation table is called
    public int evaluateContent(Mark m, int depth) {
    	//int utility = 138;
    	int sum = 0;
    	Mark enemy = m.other();
    	if(isWinner(m, lastMoveCol, lastMoveRow)){
    		sum += 10000000;
    		sum += depth;
    	} 
    	if(isWinner(enemy , lastMoveCol, lastMoveRow)){
    		sum -= 10000000;
    		sum -= depth;
    	} 
    	if(sum == 0) {
    		for (int i = 0; i < HEIGHT; i++){
    	   		for (int j = 0; j <WIDTH; j++){
    	   			if (fields[i][j].equals(m)){
    	   				sum += evaluationTable[i][j];
    	   			}
    	   			else if (fields[i][j].equals(enemy)){
    	   				sum -= evaluationTable[i][j];
    	   			}
    	   		}
    		}
 		}   	
    	return sum;//utility + sum;
    }
    
    public int evaluate(Mark m,int depth){
    	int result = 0;
    	Mark enemy = m.other();
    	    	
    	if(isWinner(m, lastMoveCol, lastMoveRow)){
    		result += 10000000;
    		result += depth;
    	} else if(isWinner(enemy , lastMoveCol, lastMoveRow)){
    		result -= 10000000;
    		result -= depth;
    	} else{
    		Map<Integer, Integer> map = getImportantFields();
    	
    		for(Entry<Integer, Integer> i: map.entrySet()){
    			if(fields[i.getKey()][i.getValue()].equals(m)){
    				result += getScore(i.getKey(),i.getValue());
    			} else {
    				result -= getScore(i.getKey(),i.getValue());
    			}
    		}
    	}
    	return result;
    }
    
    public int getScore(int height, int width){
    	int result = 0;    	
    	Mark m = fields[height][width];
    	if(height>0 && fields[height-1][width].equals(Mark.EMPTY)){
	    	if(height < HEIGHT-2 && fields[height+1][width].equals(m) && fields[height+2][width].equals(m)){
	    		result += 200;
			} else if(height < HEIGHT-1 && height>1 && fields[height+1][width].equals(m)){
				result += 200;
			} else if(height>2){
				result += 200;
			}
		}
	    if(width>0 && fields[height][width-1].equals(Mark.EMPTY)) {
	    	if(width<WIDTH-2 && fields[height][width+1].equals(m) && fields[height][width+2].equals(m)){
	    		result += 200;
			} else if(width<WIDTH-1 && width>1 && fields[height][width+1].equals(m)){
				result += 200;
			} else if(width>2){
				result += 200;
			}
		}
	    if(width<WIDTH-1 && fields[height][width+1].equals(Mark.EMPTY)) {
	    	if(width>1 && fields[height][width-1].equals(m) && fields[height][width-2].equals(m)){
	    		result += 200;
			} else if(width>0 && width<WIDTH-2 && fields[height][width-1].equals(m)){
				result += 200;
			} else if(width<WIDTH-3){
				result += 200;
			}
		}
	    if(width > 0 && height > 0 && fields[height-1][width-1].equals(Mark.EMPTY)) {
	    	if(width<WIDTH-2 && height<HEIGHT-2 && fields[height+1][width+1].equals(m) && fields[height+2][width+2].equals(m)){
	    		result += 200;
			} else if(width > 0 && height > 0 && width<WIDTH-1 && height<HEIGHT-1 && fields[height+1][width+1].equals(m)){
				result += 200;
			} else if(width > 2 && height > 2){
				result += 200;
			}
		}
	    if(width < WIDTH -1 && height > 0 && fields[height-1][width+1].equals(Mark.EMPTY)) {
	    	if(width>1 && height<HEIGHT-2 && fields[height+1][width-1].equals(m) && fields[height+2][width-2].equals(m)){
	    		result += 200;
			} else if(width < WIDTH-1 && height > 0 && width>0 && height<HEIGHT-1 && fields[height+1][width-1].equals(m)){
				result += 200;
			} else if(width < WIDTH -3 && height > 2){
				result += 200;
			}
		}
	    if(width < WIDTH -1 && height < HEIGHT-1 && fields[height+1][width+1].equals(Mark.EMPTY)) {
	    	if(width>1 && height>1 && fields[height-1][width-1].equals(m) && fields[height-2][width-2].equals(m)){
	    		result += 200;
			} else if(width < WIDTH-1 && height < HEIGHT-1 && width>0 && height>0 && fields[height-1][width-1].equals(m)){
				result += 200;
			} else if(width < WIDTH -3 && height <HEIGHT-3){
				result += 200;
			}
		}
	    if(width > 0 && height < HEIGHT-1 && fields[height+1][width-1].equals(Mark.EMPTY)) {
	    	if(width<WIDTH-2 && height>1 && fields[height-1][width+1].equals(m) && fields[height-2][width+2].equals(m)){
	    		result += 200;
			} else if(width>0 && height < HEIGHT-1 && width<WIDTH-1 && height>0 && fields[height-1][width+1].equals(m)){
				result += 200;
			} else if(width > 2 && height < HEIGHT-3){
				result += 200;
			}
		}
    	
    	return result;
    }
    
    public Map<Integer, Integer> getImportantFields(){
    	Map<Integer, Integer> result = new HashMap<Integer, Integer>();
    	for(int i = 0; i<WIDTH; i++){
    		for(int j = 0; j<HEIGHT; j++){
    			if(!fields[j][i].equals(Mark.EMPTY)){
    				if(j>0 && fields[j-1][i].equals(Mark.EMPTY))
    					result.put(j,i);
    				} else if(i>0 && fields[j][i-1].equals(Mark.EMPTY)) {
    					result.put(j,i);
    				} else if(i<WIDTH-1 && fields[j][i+1].equals(Mark.EMPTY)){
    					result.put(j,i);
    				}  else if(i<WIDTH-1 && j>0 && fields[j-1][i+1].equals(Mark.EMPTY)){
    					result.put(j,i);
    				}  else if(i<WIDTH-1 && j>0 && fields[j-1][i+1].equals(Mark.EMPTY)){
    					result.put(j,i);
    				} 
    			}
    		}
    	return result;
    }
    
    public void undoMove(){
    	fields[lastMoveRow][lastMoveCol] = Mark.EMPTY;
    }
    
    public boolean makeMove(int move, Mark m){ //moet nog error als zet niet kan
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
    		return true;
    	} else {
    		return false;
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
				break;
			}
		}
		counted = 1;
		
		while(!win && (height - counted)>=0 && (width-counted)>=0 ) {
			if(fields[height-counted][width-counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				break;
			}
		}
		counted = 1;
		steps = 1;
		
		while(!win && (height - counted)>= 0 && (width+counted)<WIDTH ) {
			if(fields[height-counted][width+counted].equals(m)){
				counted += 1;
				steps += 1;
				win = steps==counter;
			} else {
				break;
			}
		}
		counted = 1;
		
		while(!win && (height + counted)<HEIGHT && (width-counted)>=0 ) {
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
