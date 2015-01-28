package game;

import java.util.HashSet;
import java.util.Set;

/**
 * Board. 
 * @author  Roy & Edwin
 * @version 2015.01.28
 */

public class Board {


    public static final int WIDTH = 7;
    public static final int HEIGHT = 6;
    //@ invariant lastMark() == Mark.EMPTY || lastMark() == Mark.XX || lastMark() == Mark.OO;
    //@ invariant lastWidth() >= 0 && lastWidth() < WIDTH;
    //@ invariant lastHeight() >= 0 && lastHeight() < HEIGHT;
    //@ invariant getField() == Mark[HEIGHT][WIDTH];
    private Mark[][] fields;
    
    private int lastMoveCol = 0;
    private int lastMoveRow = 0;
    private Mark lastMark = Mark.EMPTY;
    
    /**
     * creates a new double array with size WIDTH and HEIGHT.
     * all marks will be Mark.EMPTY
     */
    //@ ensures \forAll int i; 0 <= i && i < HEIGHT; 
    //@ (\forAll int j; 0 <= j && j < WIDTH; (fields[i][j] == Mark.EMPTY));
    public Board() {
        fields = new Mark[HEIGHT][WIDTH];
        
        //@ loop_invariant 0<= i && i< HEIGHT;
        //@ loop_invariant 0<= j && j< WIDTH;
        //@ loop_invariant \forAll int x; 0 <= x && x < i; 
        //@ (\forAll int y; 0 <= y && y < j; fields[x][y] == Mark.EMPTY);
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                fields[i][j] = Mark.EMPTY;
            }
        }
    }
    
    /**
     * @return lastMoveRow
     */
    //@ ensures \result == lastMoveRow;
    /*@ pure */ public int lastHeight() {
        return lastMoveRow;
    }
    
    /**
     * @return lastMoveCol
     */
    //@ ensures \result == lastMoveCol;
    /*@ pure */ public int lastWidth() {
        return lastMoveCol;
    }
    
    /**
     * @return lastMark
     */
    //@ ensures \result == lastMark;
    /*@ pure */ public Mark lastMark() {
        return lastMark;
    }
    
    /**
     * @return Mark[][] fields
     */
    //@ ensures \result == fields;
    /*@ pure */ public Mark[][] getField() {
        return fields;
    }
    
    /**
     * @return Set<Integer> set with all possible moves
     */
    //@ ensures (\forAll Integer i; 0 <= i && i < WIDTH; 
    //@ \result.contains(i) ==> fields[0][i] == Mark.EMPTY;
    /*@ pure */ public Set<Integer> possibleMoves() {
        Set<Integer> moves = new HashSet<Integer>();
        //@ loop_invariant 0<= i && i< WIDTH;
        //@ loop_invariant \forAll int x; 0 <= x && x < i; 
        //@ moves.contains(x) ==> (fields[0][x] == Mark.EMPTY);
        //@ loop_invariant \result.size() >= 0;
        for (int i = 0; i < WIDTH; i++) {
            if (fields[0][i].equals(Mark.EMPTY)) {
                moves.add(i);
            }
        }
        return moves;
    }
    
    /**
     * @return Board a board with the same fields as the current board, a copy
     */
    //@ ensures \forAll int i; 0 <= i && i < HEIGHT; 
    //@ (\forAll int j; 0 <= j && j < WIDTH; result.fields[i][j] == this.fields[i][j]));
    /*@ pure */  public Board deepCopy() {
        Board result = new Board();
        //@ loop_invariant 0<= i && i< HEIGHT;
        //@ loop_invariant 0<= j && j< WIDTH;
        //@ loop_invariant \forAll int x; 0 <= x && x < i; 
        //@ (\forAll int y; 0 <= y && y < j; (result.fields[x][y] == this.fields[i][j]));
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                result.fields[i][j] = this.fields[i][j];
            }
        }
        return result;
    }
    
    /**
     * @return Boolean a boolean with true if the entire board is full
     */
    //@ ensures \result == (\forAll int i; 0 <= i && i < WIDTH; fields[0][i] != Mark.EMPTY);
    /*@ pure */ public boolean isFull() {
        boolean result = true;
        
        //@ loop_invariant 0 <= i && i < WIDTH;
        //@ loop_invariant result == true ==>(\forAll int j;
        //@ 0 <= j && j < i; fields[0][j] != Mark.EMPTY;
        //@ loop_invariant \result == false (\exists int i; 0 <= i && 
        //@ i < j; fields[0][i] == Mark.EMPTY);
        for (int i = 0; i < WIDTH; i++) {
            if (fields[0][i].equals(Mark.EMPTY)) {
                result = false;
            }
        }
        return result;
    }
    
    /**
     * @return Boolean a boolean with true if the entire board is full or 
     * if there was a winner in the last move
     */
    //@ ensures \result == (this.isWinner(lastMark, lastMoveCol, lastMoveRow) || this.isFull());
    /*@ pure */ public boolean gameOver() {
        return this.isWinner(lastMark, lastMoveCol, lastMoveRow) || this.isFull();
    }
    
    
    /**
     * sets the field with the given width and height to the given mark.
     * sets the lastMoveRow to the given height
     * sets the lastMoveCol to the given width
     * @param width the width for the field
     * @param height the height for the field
     * @param mark the mark that the field will become
     */
    //@ requires mark != null;
    //@ requires 0 <= height < HEIGHT;
    //@ requires 0 <= width < WIDTH;
    //@ ensures fields[height][width] == mark;
    //@ ensures lastMoveRow == height;
    //@ ensures lastMoveCol == width;
    public void setField(int width, int height, Mark mark) {
        lastMoveRow = height;
        lastMoveCol = width;
        fields[height][width] = mark;
    }
    
    /**
     * sets the last played move to Mark.EMPTY.
     */
    //@ requires 0 <= lastMoveRow < HEIGHT;
    //@ requires 0 <= lastMoveCol < WIDTH;
    //@ ensures fields[lastMoveRow][lastMoveCol] == Mark.EMPTY;
    public void undoMove() {
        fields[lastMoveRow][lastMoveCol] = Mark.EMPTY;
    }
    
    /**
     * @return boolean returns true if the move was done;
     * @param move the column in which the move should be done
     * @param mark the mark for the move
     */
    //@ requires 0 <= move < WIDTH;
    //@ requires mark != null;
    //@ ensures lastMoveCol = move;
    //@ ensures lastMoveRow < HEIGHT && lastMoveRow >= 0;
    //@ ensures \result == ((getField())[0][move] == Mark.EMPTY);
    //@ ensures \result == true ==> (\exists int i; 0 <= i && i < WIDTH; 
    //@ \old(getField())[i][move] == Mark.EMPTY && getField()[i][move] == mark);
    public boolean makeMove(int move, Mark mark) {
        if (fields[0][move].equals(Mark.EMPTY)) {
            boolean madeMove = false;
            int row = HEIGHT - 1;
            //@ loop_invariant 0 <= row && row < HEIGHT;
            //@ loop_invariant \forAll int j;
            //@ j < HEIGHT && j > row; fields[row][move] != Mark.EMPTY;
            while (!madeMove) {
                if (fields[row][move].equals(Mark.EMPTY)) {
                    fields[row][move] = mark;
                    lastMark = mark;
                    lastMoveRow = row;
                    lastMoveCol = move;
                    madeMove = true;
                    
                } else {
                    row--;
                }
            }
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * @return boolean returns true if the mark on fields[height][width] 
     * was a winning move for that row;
     * @param width the column in which the move was done
     * @param height the row in which the move was done
     * @param mark the mark for the move
     */
    //@ requires 0 <= width < WIDTH;
    //@ requires 0 <= height < HEIGHT;
    //@ requires mark != null;
    //@ ensures \result == true || \result == false;
    /*@ pure */ public boolean hasRow(Mark mark, int width, int height) {
        boolean result = false;
        int counter = 0;
        int startWidth = width - 3;
        if (startWidth < 0) {
            startWidth = 0;
        }
        //@ loop_invariant 0 <= i && i < WIDTH;
        //@ loop_invariant counter >= 0;
        //@ loop_invariant startWidth >= 0 && startWidth <= WIDTH;
        for (int i = 0; i < WIDTH; i++) {
            if (startWidth + i >= WIDTH) {
                break;
            }
            if (fields[height][startWidth + i].equals(mark)) {
                counter++;
            } else {
                counter = 0;
            }
            if (counter >= 4) {
                result = true;
            }
        }
        return result;
    }
    
    /**
     * @return boolean returns true if the mark on fields[height][width] 
     * was a winning move for that column;
     * @param width the column in which the move was done
     * @param height the row in which the move was done
     * @param mark the mark for the move
     */
    //@ requires 0 <= width < WIDTH;
    //@ requires 0 <= height < HEIGHT;
    //@ requires mark != null;
    //@ ensures \result == (getField[width][height+1] == mark &&
    //@ getField[width][height+2] == mark && getField[width][height+3] == mark;
    /*@ pure */ public boolean hasColumn(Mark mark, int width, int height) {
        
        boolean win = false;
        int counter = 3;
        int counted = 1;
        
        if (height > 2) {
            win = false;
        } else {
        	//@ loop_invariant counted <= 3 && counted >= 0;
            //@ loop_invariant startWidth >= 0 && startWidth <= WIDTH;
        	//@ loop_invariant win == (counted >= counter);
            while (!win) {
                if (fields[height + counted][width].equals(mark)) {
                    win = counted >= counter;
                    counted++;
                } else {
                    break;
                }
            }
        }
        return win;
    }
    
    /**
     * @return boolean returns true if the mark on fields[height][width] 
     * was a winning move for a diagonal;
     * @param width the column in which the move was done
     * @param height the row in which the move was done
     * @param mark the mark for the move
     */
    //@ requires 0 <= width < WIDTH;
    //@ requires 0 <= height < HEIGHT;
    //@ requires mark != null;
    //@ ensures \result == true || \result == false;
    /*@ pure */ public boolean hasDiagonal(Mark mark, int width, int height) {
        boolean win = false;
        int counter = 4;
        int counted = 1;
        int steps = 1;
        
        //@ loop_invariant counted <= 4 && counted >= 1;
        //@ loop_invariant steps >= 1;
        //@ loop_invariant (height + counted) < HEIGHT;
        //@ loop_invariant (width + counted) < WIDTH;
        //@ loop_invariant win = (steps == counter);
        while (!win && (height + counted) < HEIGHT && (width + counted) < WIDTH) {
            if (fields[height + counted][width + counted].equals(mark)) {
                counted += 1;
                steps += 1;
                win = steps == counter;
            } else {
                break;
            }
        }
        counted = 1;
        
        //@ loop_invariant counted <= 4 && counted >= 1;
        //@ loop_invariant steps >= 1;
        //@ loop_invariant (height - counted) >= 0;
        //@ loop_invariant (width - counted) >= 0;
        //@ loop_invariant win = (steps == counter);
        while (!win && (height - counted) >= 0 && (width - counted) >= 0) {
            if (fields[height - counted][width - counted].equals(mark)) {
                counted += 1;
                steps += 1;
                win = steps == counter;
            } else {
                break;
            }
        }
        counted = 1;
        steps = 1;
        
        //@ loop_invariant counted <= 4 && counted >= 1;
        //@ loop_invariant steps >= 1;
        //@ loop_invariant (height - counted) >= 0;
        //@ loop_invariant (width + counted) < WIDTH;
        //@ loop_invariant win = (steps == counter);
        while (!win && (height - counted) >= 0 && (width + counted) < WIDTH) {
            if (fields[height - counted][width + counted].equals(mark)) {
                counted += 1;
                steps += 1;
                win = steps == counter;
            } else {
                break;
            }
        }
        counted = 1;
        
        //@ loop_invariant counted <= 4 && counted >= 1;
        //@ loop_invariant steps >= 1;
        //@ loop_invariant (height + counted) < HEIGHT;
        //@ loop_invariant (width - counted) >= 0;
        //@ loop_invariant win = (steps == counter);
        while (!win && (height + counted) < HEIGHT && (width - counted) >= 0) {
            if (fields[height + counted][width - counted].equals(mark)) {
                counted += 1;
                steps += 1;
                win = steps == counter;
            } else {
                break;
            }
        }
        
        return win;
    }
    
    /**
     * @return Boolean a boolean with true if the mark has a row, column, diagonal for that move.
     * @param mark the mark to check for
     * @param width the width to check for
     * @param height the height to check for
     */
    //@ requires 0 <= width < WIDTH;
    //@ requires 0 <= height < HEIGHT;
    //@ requires mark != null; 
    //@ ensures \result == (this.hasRow(mark, width, height) || this.hasCol(mark, width, height)
    //@ || this.hasDiagonal(mark, width, height));
    /*@ pure */ public boolean isWinner(Mark mark, int width, int height) {
        return this.hasRow(mark, width, height) 
                || this.hasColumn(mark, width, height) 
                || this.hasDiagonal(mark, width, height);
    }
    
    /**
     * @return Boolean a boolean with true if there was a winner for the move.
     * @param width the width to check for
     * @param height the height to check for
     */
    //@ requires 0 <= width < WIDTH;
    //@ requires 0 <= height < HEIGHT;
    //@ ensures \result == (isWinner(Mark.XX, width, height) || isWinner(Mark.OO, width, height));
    /*@ pure */ public boolean hasWinner(int width, int height) {
        return isWinner(Mark.XX, width, height) || isWinner(Mark.OO, width, height);
    }
}
