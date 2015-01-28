package strategies;


import game.Board;
import game.Mark;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

/**
 * AI. 
 * @author  Roy en Edwin
 * @version 2015.01.28
 */

public class SmartStrategy implements Strategy {

	public static final String NAME = "Smart";
	int depth;
	
	/** 
	 * Start een nieuwe SmartStrategy op.
	 * @param d the amount of moves the strategy will think ahead
	 */
	
	public SmartStrategy(int d) {
		depth = d * 2;
	}
	
	/**
	 * @return name returns the name
	 */
	public String getName() {
		return NAME;
	}
	
	/**
	 * Calculates and returns the best move using a thread
	 * for every possible move.
	 * each thread calculates the value of the move using alphabeta
	 * then the move with the best value is chosen
	 * @param b the current state of the board
	 * @param m the mark of the player
	 */
	public int determineMove(Board b, Mark m) {
		Board copyOfBoard = b.deepCopy();
		
		Set<Integer> moves = copyOfBoard.possibleMoves();
		final int[] results = new int[7];
		for (int p = 0; p < 7; p++) {
			results[p] = Integer.MIN_VALUE;
		}
		Set<Thread> threads = new HashSet<Thread>();
		for (int i: moves) {
			threads.add(new Thread() {
				
				public void run() {
	        		
	        		Board copy = copyOfBoard.deepCopy();
	        		copy.makeMove(i, m);
	        		int z = alphaBeta(copy, m, Integer.MIN_VALUE, Integer.MAX_VALUE, depth, false);
	        		
	        		results[i] = z;
	        		copy.undoMove();
	        	}
	        	
			});
		}
		for (Thread t: threads) {
			t.start();
		}
		try {
			for (Thread t: threads) {
				t.join();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		int result = 3;
		int biggest = results[3];
		for (int i = 0; i < 7; i++) {
			if (results[i] > biggest) {
				biggest = results[i];
				result = i;
			}
		}
		while (!moves.contains(result)) {
			result = (result + 1) % 7;
		}
		return result;
	}
	
	/**
	 * Calculates and returns the value the move based on
	 * the assumption that you will always take the best move
	 * and the enemy the worst move for you
	 * this is calculated with alphabeta pruning.
	 * @param b the state of the board
	 * @param m the mark of the player
	 * @param alpha the alpha value in the alphabeta pruning
	 * @param beta the beta value in the alphabeta pruning
	 * @param depthz the depth is how many moves you are looking ahead of the current game
	 * @param myTurn is it my turn or my enemy's turn
	 */
	
	public int alphaBeta(Board b, Mark m, int alpha, int beta, int depthz, boolean myTurn) {
		Board copyOfBoard = b.deepCopy();
		Set<Integer> moves = copyOfBoard.possibleMoves();
		Mark enemy = m.other();
		if (b.gameOver() || depthz == 0) {
			return evaluate(m, depthz, b);
		} else if (myTurn) {
			int v = Integer.MIN_VALUE;
			int a = alpha;
			for (int i: moves) {
				copyOfBoard.makeMove(i, m);
				int z = alphaBeta(copyOfBoard, m, a, beta, depthz - 1, false);
				copyOfBoard.undoMove();
				if (z > v) {
					v = z;
				}
				if (v > a) {
					a = v;
				}
				if (beta <= a) {
					break;
				}
			}
			return v;
		} else {
			int v = Integer.MAX_VALUE;
			int be = beta;
			for (int i: moves) {
				copyOfBoard.makeMove(i, enemy);
				int z = alphaBeta(copyOfBoard, m, alpha, be, depthz - 1, true);
				copyOfBoard.undoMove();
				if (z < v) {
					v = z;
				}
				if (v < be) {
					be = v;
				}
				if (be <= alpha) {
					break;
				}
			}
			return v;
		}
	}
	
	private static int[] evaluateCol = {1, 5, 10, 70, 10, 5, 1};
    
	/**
	 * Calculates and returns the value of the board for mark
	 * first it looks if a player has won.
	 * then it gives adds a score based on how close to the middle
	 * the marks are, and if there are any row with only one player's marks
	 * after that a score is added based on amount of winning moves for you/your opponent
	 * @param mark Mark that is used by the player
	 * @param depths the depth is how many moves you are looking ahead of the current game
	 * @param board the board situation
	 * @return int De waarde voor de positie op het bord
	 */
	
    public int evaluate(Mark mark, int depths, Board board) {
        int result = 0;
        Mark enemy = mark.other();
        Mark[][] fields = board.getField();
        if (board.isWinner(mark, board.lastWidth(), board.lastHeight())) {
            result += 10000000;
            result += depths * 100000;
        } 
        if (board.isWinner(enemy , board.lastWidth(), board.lastHeight())) {
            result -= 10000000;
            result -= depths * 100000;
        } 
        if (result == 0) {
            for (int i = 0; i < Board.HEIGHT; i++) {
                Mark rowMark = Mark.EMPTY;
                boolean gotRow = true;
                for (int j = 0; j < Board.WIDTH; j++) {
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

            Map<Integer, Integer> winningFields = getImportantFields(mark, board);
            Map<Integer, Integer> loosingFields = getImportantFields(enemy, board);
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
    
    /**
	 * Calculates and returns a map with the height/width of a field,
	 * the fields it returns are empty but would win the game for this mark.
	 * @param mark Mark that is used by the player
	 * @param b the board situation
	 * @return Map<int, int> De map van.
	 */
    public Map<Integer, Integer> getImportantFields(Mark mark, Board b) {
    	Mark[][] fields = b.getField();
        Map<Integer, Integer> result = new HashMap<Integer, Integer>();
        for (int i = 0; i < Board.WIDTH; i++) {
            for (int j = 0; j < Board.HEIGHT; j++) {
                if (fields[j][i].equals(Mark.EMPTY)) {
                    b.setField(i, j, mark);
                    if (b.isWinner(mark, i, j)) {
                        result.put(j, i);
                    }
                    b.undoMove();
                }
            }
        }
        return result;
    }
}
