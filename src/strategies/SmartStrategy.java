package strategies;


import game.Board;
import game.Mark;

import java.util.HashSet;
import java.util.Set;

public class SmartStrategy implements Strategy{

	public static final String NAME="Smart";
	int depth;
	
	public SmartStrategy(int d){
		depth = d*2;
	}
	
	public String getName() {
		return NAME;
	}
	
	public int determineMove(Board b, Mark m){
		Board copyOfBoard = b.deepCopy();
		
		Set<Integer> moves = copyOfBoard.possibleMoves();
		final int[] results = new int[7];
		for(int p = 0; p<7;p++){
			results[p] = Integer.MIN_VALUE;
		}
		Set<Thread> threads = new HashSet<Thread>();
		for(int i: moves){
			threads.add(new Thread() {
				
				public void run() {
	        		
	        		Board copy = copyOfBoard.deepCopy();
	        		copy.makeMove(i, m);
	        		int z = alphaBeta(copy, m,Integer.MIN_VALUE, Integer.MAX_VALUE, depth, false);
	        		
	        		results[i] = z;
	        		
	        		//results[i] = alphaBeta(copy, enemy,Integer.MIN_VALUE, Integer.MAX_VALUE, depth, false);
	        		copy.undoMove();
	        	}
	        	
			});
		}
		for(Thread t: threads){
			t.start();
		}
		try {
			for(Thread t: threads){
				t.join();
			}
		} catch(InterruptedException e){
			
		}
		int result = 3;
		int biggest = results[3];
		for(int i = 0; i<7; i++){
			if(results[i]>biggest){
				biggest = results[i];
				result = i;
			}
		}
		while(!moves.contains(result)){
			result = (result+1)%7;
		}
		return result;
	}
	
	public int alphaBeta(Board b, Mark m,int alpha, int beta, int depth, boolean myTurn){
		Board copyOfBoard = b.deepCopy();
		Set<Integer> moves = copyOfBoard.possibleMoves();
		Mark enemy = m.other();
		if(b.gameOver() || depth == 0){
			return b.evaluate(m, depth);
		} else if(myTurn){
			int v = Integer.MIN_VALUE;
			int a = alpha;
			for(int i: moves){
				copyOfBoard.makeMove(i, m);
				int z = alphaBeta(copyOfBoard, m, a, beta, depth - 1, false);
				copyOfBoard.undoMove();
				if(z>v){
					v = z;
				}
				if(v>a){
					a = v;
				}
				if(beta <= a){
					break;
				}
			}
			return v;
		} else {
			int v = Integer.MAX_VALUE;
			int be = beta;
			for(int i: moves){
				copyOfBoard.makeMove(i, enemy);
				int z = alphaBeta(copyOfBoard, m, alpha, be, depth - 1, true);
				copyOfBoard.undoMove();
				if(z<v){
					v = z;
				}
				if(v<be){
					be = v;
				}
				if(be <= alpha){
					break;
				}
			}
			return v;
		}
	}
}
