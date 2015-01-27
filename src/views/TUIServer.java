package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TUIServer {
	private BufferedReader in;
	
	public TUIServer() {
		in = new BufferedReader(new InputStreamReader(System.in));
	}
		
	public void message(String message) {
		System.out.println(message);
	}
	
	public String readString(String tekst) {
		
		System.out.print(tekst);
		String antw = null;
		try {
			antw = in.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (antw == null) ? "" : antw;
	}
}
