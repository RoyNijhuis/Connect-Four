package views;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class TUIServer implements Observer {
	private BufferedReader in;
	
	public TUIServer(){
		in = new BufferedReader(new InputStreamReader(System.in));
	}
	public void update(Observable o, Object arg) {
		
		
		if(arg.equals("printBoard")) {
			
		}
	}
	
	public void message(String message){
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
