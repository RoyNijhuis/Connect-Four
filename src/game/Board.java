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
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                fields[i][j] = Mark.EMPTY;
            }
        }
    }
    
    public int lastHeight() {
        return lastMoveRow;
    }
    
    public int lastWidth() {
        return lastMoveCol;
    }
    
    public Mark lastMark() {
        return lastMark;
    }
    
    public Mark[][] getField() {
        return fields;
    }
    
    public Set<Integer> possibleMoves() {
        Set<Integer> moves = new HashSet<Integer>();
        for (int i = 0; i < WIDTH; i++) {
            if (fields[0][i].equals(Mark.EMPTY)) {
                moves.add(i);
            }
        }
        return moves;
    }
    
    public Board deepCopy() {
        Board result = new Board();
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                result.fields[i][j] = this.fields[i][j];
            }
        }
        return result;
    }
    
    public boolean isFull() {
        boolean result = true;
        for (int i = 0; i < WIDTH; i++) {
            if (fields[0][i].equals(Mark.EMPTY)) {
                result = false;
            }
        }
        return result;
    }

    public boolean gameOver() {
        return this.isWinner(lastMark, lastMoveCol, lastMoveRow) || this.isFull();
    }
    
    private static int[] evaluateCol = {1, 5, 10, 70, 10, 5, 1};
    
    public int evaluate(Mark mark, int depth) {
        int result = 0;
        Mark enemy = mark.other();
        if (isWinner(mark, lastMoveCol, lastMoveRow)) {
            result += 10000000;
            result += depth * 100000;
        } 
        if (isWinner(enemy , lastMoveCol, lastMoveRow)) {
            result -= 10000000;
            result -= depth * 100000;
        } 
        if (result == 0) {
            for (int i = 0; i < HEIGHT; i++) {
                Mark rowMark = Mark.EMPTY;
                boolean gotRow = true;
                for (int j = 0; j < WIDTH; j++) {
                    if (fields[i][j].equals(mark)) {
                        result += evaluateCol[j];
                        if (gotRow && !rowMark.equals(enemy)) {
                            rowMark = mark;
                        } else {
                            gotRow = false;
                        }
                           
                    } else if (fields[i][j].equals(enemy)) {
                        result -= evaluateCol[j];
                        if (gotRow && !rowMark.equals(mark)) {
                            rowMark = enemy;
                        } else {
                            gotRow = false;
                        }
                    }
                }
                if (gotRow) {
                    if (rowMark.equals(mark)) {
                        result += 100;
                    } else if (rowMark.equals(enemy)) {
                        result -= 80;
                    }
                }
            }

            Map<Integer, Integer> winningFields = getImportantFields(mark);
            Map<Integer, Integer> loosingFields = getImportantFields(enemy);
            int multiplier = mark.equals(Mark.XX) ? 2 : 1;
            int multiplier2 = mark.equals(Mark.XX) ? 1 : 2;
            
            
            for (Entry<Integer, Integer> win: winningFields.entrySet()) {
                if (win.getKey() % 2 == 0) {
                    result += 100 * multiplier;
                } else {
                    result += 100 * multiplier2;
                }
            }
            for (Entry<Integer, Integer> loss: loosingFields.entrySet()) {
                if (loss.getKey() % 2 == 0) {
                    result -= 100 * multiplier2;
                } else {
                    result -= 100 * multiplier;
                }
            }
        }   
        
        return result;
    }
    
    public Map<Integer, Integer> getImportantFields(Mark mark) {
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                if (fields[j][i].equals(Mark.EMPTY)) {
                    setField(i, j, mark);
                    if (isWinner(mark, i, j)) {
                        result.put(j, i);
                    }
                    undoMove();
                }
            }
        }
        return result;
    }
    
    public void setField(int width, int height, Mark mark) {
        lastMoveRow = height;
        lastMoveCol = width;
        fields[height][width] = mark;
    }
    
    public void undoMove() {
        fields[lastMoveRow][lastMoveCol] = Mark.EMPTY;
    }
    
    public boolean makeMove(int move, Mark mark) {
        if (fields[0][move].equals(Mark.EMPTY)) {
            boolean madeMove = false;
            int row = HEIGHT - 1;
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
    
    public boolean hasRow(Mark mark, int width, int height) {
        boolean result = false;
        int counter = 0;
        int startWidth = width - 3;
        if (startWidth < 0) {
            startWidth = 0;
        }
        
        for (int i = 0; i < 7; i++) {
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

    public boolean hasColumn(Mark mark, int width, int height) {
        
        boolean win = false;
        int counter = 3;
        int counted = 1;
        
        if (height > 2) {
            win = false;
        } else {
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

    public boolean hasDiagonal(Mark mark, int width, int height) {
        boolean win = false;
        int counter = 4;
        int counted = 1;
        int steps = 1;
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

    public boolean isWinner(Mark mark, int width, int height) {
        return this.hasRow(mark, width, height) 
                || this.hasColumn(mark, width, height) 
                || this.hasDiagonal(mark, width, height);
    }

    public boolean hasWinner(int width, int height) {
        return isWinner(Mark.XX, width, height) || isWinner(Mark.OO, width, height);
    }

    public String toString() {
        return null;
    }
}
