package views;
import game.Board;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import network.Client;
import players.Player;


public class GUI extends JFrame implements View, ActionListener{

	JPanel localOrOnline;
	JPanel askName;
	String localOnline;
	String nameChosen;
	JButton localBtn, onlineBtn, submitBtn;
	JLabel label;
	JPanel waitForGame;
	JPanel game;
	JTextField txt;
	JLabel errorField;
	BufferedImage boardImage;
	JLabel boardLabel;
	
	public GUI() {
		nameChosen = null;
		localOrOnline = new JPanel();
		waitForGame = new JPanel();
		askName = new JPanel();
		game = new JPanel();
		localOnline = null;
		label = new JLabel("Waiting for a game...");
		errorField = new JLabel("");
		this.setSize(400, 400);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.add(localOrOnline);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			printBoard(o);
		} else if(arg.equals("gameOver")) {
			//gameOver(o);
		} else if(arg.equals("columnFull")) {
			//System.out.println("This column is full...");
		} else if(arg.equals("badHost")) {
			errorField.setText("Host is niet goed...");
		} else if(arg.equals("cannotCreateClient")) {
			errorField.setText("Kan Client niet aanmaken...");
		}else if(arg.equals("askPlayAgain")) {
			//askPlayAgain();
		} else if(arg.equals("draw")) {
			//draw();
		} else if(((String)arg).startsWith("accepted")) {
			System.out.println("accepted!");
			this.remove(askName);
			label = new JLabel();
			label.setText("Looking for a game...");
			waitForGame.add(label);
			this.add(waitForGame);
			revalidate();
			repaint();
		} else if(((String)arg).startsWith("nameExists")) {
			errorField.setText("There already exists a player with this name on the server...");
		} else if(((String)arg).startsWith("gameStarted")) {
			//setup game panel
			setupBoard();
			printBoard(null);
		}
	}
	
	private void setupBoard() {
		try {
			boardImage = ImageIO.read(new File("board.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boardLabel = new JLabel(new ImageIcon(boardImage));
	}
	
	private void printBoard(Observable o) {
		if(o == null) {
			//print empty board, no move has been requested or made
			this.remove(waitForGame);
			game.add(boardLabel);
			this.add(game);
			revalidate();
			repaint();
		} else {
			
		}
	}

	@Override
	public int getHumanMove(String s) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Player[] askForPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String askLocalOrOnline() {
		localBtn = new JButton("Local");
		onlineBtn = new JButton("Online");
		localBtn.addActionListener(this);
		onlineBtn.addActionListener(this);
		localOrOnline.add(localBtn);
		localOrOnline.add(onlineBtn);
		localOrOnline.add(errorField);
		while(localOnline == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return localOnline;
	}

	@Override
	public String askPlayerName() {
		this.remove(localOrOnline);
		JLabel label = new JLabel("Enter your name: ");
		txt = new JTextField();
		txt.setColumns(10);
		submitBtn = new JButton("Connect");
		
		submitBtn.addActionListener(this);
		askName.add(label);
		askName.add(txt);
		askName.add(submitBtn);
		errorField = new JLabel();
		askName.add(errorField);
		this.add(askName);
		revalidate();
		repaint();
		while(nameChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return nameChosen;
	}

	@Override
	public void setClient(Client client) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(localBtn)) {
			localOnline = "local";
		} else if(e.getSource().equals(onlineBtn)) {
			localOnline = "online";
			System.out.println("online gekozen");
		} else if(e.getSource().equals(submitBtn)) {
			nameChosen = txt.getText();
		} 
		System.out.println("test");
	}
}
