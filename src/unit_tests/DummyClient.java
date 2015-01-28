package unit_tests;

import game.Mark;
import game.NetworkGame;
import players.NetworkPlayer;
import players.Player;
import views.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Observable;

public class DummyClient {
	
	private Socket sock;
	private BufferedReader in;
	private BufferedWriter out;
	
	public DummyClient(InetAddress host, int port){
		try {
			this.sock = new Socket(host, port);
			in = new BufferedReader(new InputStreamReader(sock.getInputStream(), "UTF-8"));
			out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream(), "UTF-8"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String receiveMessage() {
		String line = null;
		try {
			line = in.readLine();
		} catch (IOException e) {
			try {
				sock.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return line;
	}
	
	public void sendMessage(String msg) {
		try {
			out.write(msg + "\n");
			out.flush();
		} catch (IOException e) {
			try {
				sock.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void shutdown() {
		try {
			sock.close();
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

