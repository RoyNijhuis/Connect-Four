package unit_tests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.junit.Before;
import org.junit.Test;

public class ServerAndClientHandlerTest {
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
	public void testBadCommand() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join Roy 12");
		c1.receiveMessage();
		c1.sendMessage("blablabla slecht commando");
		assertEquals("error 007", c1.receiveMessage());
		c1.shutdown();
		c1 = null;
	}
	
	@Test
	public void testConnectWithJoinCommand() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join Roy2 12");
		assertEquals("accept 12 Chat", c1.receiveMessage());
		c1 = null;
	}
	
	@Test
	public void testConnectWithBadJoinCommand() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join 12");
		assertEquals("error 007", c1.receiveMessage());
		c1 = null;
	}
	
	@Test
	public void testGlobalChat() {
		c1 = new DummyClient(host, 2727);
		c1.sendMessage("join Roy23 12 Chat");
		c1.receiveMessage();
		c1.sendMessage("chat_global Dit is een test!");
		assertEquals("message Roy23 Dit is een test!", c1.receiveMessage());
		c1 = null;
	}
}
