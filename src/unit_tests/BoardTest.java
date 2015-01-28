package unit_tests;

import static org.junit.Assert.*;
import game.Board;
import game.Mark;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

	@Before
	public void setUp() throws Exception {
		
	}

	@Test
	public void testRepresentationOfBoardAtConstruction() {
		Board b = new Board();
		Mark[][] repres = b.getField();
		for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	assertEquals(repres[i][j], Mark.EMPTY);
            }
        }
	}
	
	@Test
	public void tesFullWhenFull() {
		Board b = new Board();
		Mark[][] repres = b.getField();
		for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	repres[i][j] = Mark.XX;
            }
        }
		assertEquals(true, b.isFull());
	}
	
	@Test
	public void tesFullWhenNotFull() {
		Board b = new Board();
		Mark[][] repres = b.getField();
		for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
            	repres[i][j] = Mark.XX;
            }
        }
		b.setField(0, 0, Mark.EMPTY);
		assertEquals(false, b.isFull());
	}
	
	@Test
	public void tesGameOverWhenGameOver() {
		Board b = new Board();
		Mark[][] repres = b.getField();
		b.makeMove(0, Mark.XX);
		b.makeMove(1, Mark.XX);
		b.makeMove(2, Mark.XX);
		b.makeMove(3, Mark.XX);
		assertEquals(true, b.gameOver());
	}
	
	@Test
	public void tesGameOverWhenNotGameOver() {
		Board b = new Board();
		Mark[][] repres = b.getField();
		b.makeMove(0, Mark.XX);
		b.makeMove(1, Mark.XX);
		b.makeMove(2, Mark.XX);
		assertEquals(false, b.gameOver());
	}
	
	public void test() {
		fail("Not yet implemented");
	}
}
