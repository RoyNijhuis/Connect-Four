package unit_tests;

import static org.junit.Assert.*;

import java.net.InetAddress;

import org.junit.Before;
import org.junit.Test;

public class ServerGameTest {

	private DummyClient c1, c2, c3, c4;
	private InetAddress host;
	
	@Before
	public void setUp() throws Exception {
		//NOTE: Er wordt bij elke test vanuit gegegaan dat de server AAN staat, 
		//      tenzij dit anders vermeldt is. (op poort 2727 en ip: localhost)
		c1 = null;
		c2 = null;
		c3 = null;
		c4 = null;
		host = InetAddress.getByName("localhost");
	}

	@Test
	public void testStartGame() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join Roy2 12");
		c1.receiveMessage();
		c1.sendMessage("ready_for_game");
		c2 = new DummyClient(host, 2727);
		c2.sendMessage("join Roy3 12");
		c2.receiveMessage();
		c2.sendMessage("ready_for_game");
		assertEquals("start_game Roy3 Roy2", c1.receiveMessage());
		c1.shutdown();
		c2.shutdown();
		c1 = null;
		c2 = null;
	}
	
	@Test
	public void testMakeMove() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join Roy123 12");
		c1.receiveMessage();
		c1.sendMessage("ready_for_game");
		c2 = new DummyClient(host, 2727);
		c2.sendMessage("join Roy1234 12");
		c2.receiveMessage();
		c2.sendMessage("ready_for_game");
		c2.receiveMessage();
		c2.receiveMessage();
		c2.sendMessage("do_move 1");
		c1.receiveMessage();
		c1.receiveMessage();
		assertEquals("done_move Roy1234 1", c1.receiveMessage());
		c1.shutdown();
		c2.shutdown();
		c1 = null;
		c2 = null;
	}
}
