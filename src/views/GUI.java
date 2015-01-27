package views;
import game.Board;
import game.Game;
import game.Mark;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import network.Client;
import players.Player;


public class GUI extends JFrame implements View, ActionListener, MouseListener{

	JPanel localOrOnline;
	JPanel askName;
	String localOnline;
	String nameChosen;
	JButton localBtn, onlineBtn, submitBtn, sendChatMessage, submitPlayersButton;
	JLabel label, playerName;
	JLabel choosePlayers;
	JPanel waitForGame;
	JPanel game;
	JTextField txt, chatMessage;
	JTextArea chat;
	JLabel errorField, messageField;
	BufferedImage boardImage, XXImage, OOImage;
	JLabel boardLabel;
	JRadioButton local, global;
	ButtonGroup buttonGroup;
	ArrayList<JLabel> marks;
	private int moveMade;
	private boolean askingMove;
	private Player[] playersChosen;
	Client client;
	
	public GUI() {
		moveMade = -1;
		playersChosen = new Player[2];
		marks = new ArrayList<JLabel>();
		askingMove = false;
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
		try {
			XXImage = ImageIO.read(new File("XX.png"));
			OOImage = ImageIO.read(new File("OO.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if(arg.equals("printBoard")) {
			printBoard(o);
		} else if(arg.equals("gameOver")) {
			messageField.setText("Game over! The winner is: " + ((Game)o).getWinner().getName());
		} else if(arg.equals("columnFull")) {
			errorField.setText("This column is full");
		} else if(arg.equals("badHost")) {
			errorField.setText("Host is niet goed...");
		} else if(arg.equals("cannotCreateClient")) {
			errorField.setText("Kan Client niet aanmaken...");
		}else if(arg.equals("askPlayAgain")) {
			//askPlayAgain();
		} else if(arg.equals("draw")) {
			messageField.setText("Game over! There is no winner, it is a draw...");
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
			this.setSize(800, 800);
			setupBoard();
			printBoard(null);
		} else if(((String)arg).startsWith("message")) {
			String[] splitString = ((String)arg).split(" ", 3);
			String name = splitString[1];
			String message = splitString[2];
			chat.setText(chat.getText() + "\n" + name + " says: " + message);
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
		boardLabel.addMouseListener(this);
	}
	
	private void printBoard(Observable o) {
		if(o == null) {
			//print empty board, no move has been requested or made
			this.remove(waitForGame);
			game.setLayout(null);
			chat = new JTextArea();
			messageField = new JLabel();
			buttonGroup = new ButtonGroup();
			local = new JRadioButton("Local");
			global = new JRadioButton("Global");
			buttonGroup.add(local);
			buttonGroup.add(global);
			chat.setLineWrap(true);
			errorField = new JLabel();
			errorField.setForeground(Color.RED);
			playerName = new JLabel();
			sendChatMessage = new JButton("Send");
			sendChatMessage.addActionListener(this);
			chatMessage = new JTextField();
			chat.setBounds(20,525,640,200);
			chat.setEditable(false);
			messageField.setBounds(330,510,300,10);
			boardLabel.setBounds(20, 20, 640, 480);
			errorField.setBounds(20, 510, 300, 10);
			chatMessage.setBounds(40, 730,300,25);
			playerName.setBounds(5, 730, 50, 10);
			sendChatMessage.setBounds(350,730,80, 25);
			local.setBounds(450, 730, 75, 25);
			global.setBounds(550, 730, 75, 25);
			local.setSelected(true);
			errorField.setText("This is an error");
			messageField.setText("This is a message");
			playerName.setText("Chat: ");
			game.add(boardLabel);
			game.add(messageField);
			game.add(chat);
			game.add(errorField);
			game.add(playerName);
			game.add(chatMessage);
			game.add(local);
			game.add(global);
			game.add(sendChatMessage);
			this.add(game);
			revalidate();
			repaint();
		} else {
			errorField.setText("");
			messageField.setText("");
			Board board = ((Game) o).getBoard();
			Mark[][] field = board.getField();
			for(int hor=0;hor<7;hor++) {
				for(int ver=0;ver<6;ver++) {
					JLabel temp = null;
					if(field[ver][hor].equals(Mark.XX)) {
						temp = new JLabel(new ImageIcon(XXImage));
					} else if(field[ver][hor].equals(Mark.OO)) {
						temp = new JLabel(new ImageIcon(OOImage));
					}
					//draw mark
					if(temp != null) {
						temp.setBounds(35+hor*90, 26+ver*80, 70, 70);
						game.add(temp);
					}
				}
			}
			revalidate();
			repaint();
		}
	}

	@Override
	public int getHumanMove(String s) {
		askingMove = true;
		while(moveMade == -1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		int temp = moveMade;
		moveMade = -1;
		askingMove = false;
		return temp;
	}

	public Player[] askForPlayers() {
		choosePlayers = new JLabel();
		localOrOnline.removeAll();
		JLabel name1 = new JLabel("Player 1: ");
		JLabel name2 = new JLabel("Player 1: ");
		submitPlayersButton = new JButton("Online");
		submitPlayersButton.addActionListener(this);
		choosePlayers.add(name1);
		choosePlayers.add(name2);
		choosePlayers.add(submitPlayersButton);
		revalidate();
		repaint();
		
		while(playersChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Player[] temp = playersChosen;
		playersChosen = null;
		return temp;
	}

	@Override
	public String askLocalOrOnline() {
		localOrOnline.removeAll();
		localBtn = new JButton("Local");
		onlineBtn = new JButton("Online");
		localBtn.addActionListener(this);
		onlineBtn.addActionListener(this);
		localOrOnline.add(localBtn);
		localOrOnline.add(onlineBtn);
		localOrOnline.add(errorField);
		revalidate();
		repaint();
		while(localOnline == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String temp = localOnline;
		localOnline = null;
		return temp;
	}

	@Override
	public String askPlayerName() {
		this.remove(localOrOnline);
		askName.removeAll();
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
		System.out.println("name is not yet chosen");
		while(nameChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("name is chosen");
		String temp = nameChosen;
		nameChosen = null;
		return temp;
	}

	@Override
	public void setClient(Client client) {
		this.client = client;
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
		} else if(e.getSource().equals(sendChatMessage)) {
			String txt = chatMessage.getText();
			if(global.isSelected()) {
				client.sendMessage("chat_global " + txt);
			} else if(local.isSelected()) {
				client.sendMessage("chat_local " + txt);
			}
		} else if(e.getSource().equals(submitPlayersButton)) {
			//String player1 = //check players and type of players(human/ai)
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource().equals(boardLabel)) {
			System.out.println("Clicked on board!");
			//clicked on board
			int clickX = e.getX();
			if(askingMove) {
				if(clickX>=0 && clickX < 95) {
					//row 1
					moveMade=0;
				} else if(clickX>=95 && clickX < 185) {
					//row 2
					moveMade=1;
				} else if(clickX>=185 && clickX < 275) {
					//row 2
					moveMade=2;
				} else if(clickX>=275 && clickX < 365) {
					//row 3
					moveMade=3;
				} else if(clickX>=365 && clickX < 455) {
					//row 4
					moveMade=4;
				} else if(clickX>=455 && clickX < 545) {
					//row 5
					moveMade=5;
				} else if(clickX>=545 && clickX < 640) {
					//row 6
					moveMade=6;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void giveHint(int i) {
		// TODO Auto-generated method stub
		
	}
}
