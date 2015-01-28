package views;
import game.Board;
import game.ConnectFour;
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
import players.ComputerPlayer;
import players.HumanPlayer;
import players.Player;
import strategies.SmartStrategy;


public class GUI extends JFrame implements View, ActionListener, MouseListener {

	private static final long serialVersionUID = 1;
	JPanel localOrOnline;
	JPanel askName, askServer, askPort;
	String localOnline;
	String nameChosen, iPChosen, portChosen;
	JButton localBtn, onlineBtn, submitBtn, sendChatMessage;
	JButton submitPlayersButton, homeMenu, connectBtn;
	JLabel label, playerName, difficultyLabel;
	JLabel choosePlayers;
	JPanel waitForGame;
	JPanel game;
	JTextField txt, chatMessage, player1, player2, ipText;
	JTextField portText, difficultyText, difficultyText2;
	JTextArea chat;
	JLabel errorField, messageField, hint;
	BufferedImage boardImage, xXImage, oOImage;
	JLabel boardLabel;
	JRadioButton local, global, human1, human2, aI1, aI2;
	ButtonGroup buttonGroup, player1Group, player2Group;
	ArrayList<JLabel> marks;
	private Player playerChosen;
	private int moveMade;
	private boolean askingMove;
	private Player[] playersChosen;
	Client client;
	public static boolean retry = false;
	
	public GUI() {
		super();
		moveMade = -1;
		playersChosen = null;
		iPChosen = null;
		portChosen = null;
		playerChosen = null;
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
			xXImage = ImageIO.read(new File("XX.png"));
			oOImage = ImageIO.read(new File("OO.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		moveMade = -1;
		playersChosen = null;
		iPChosen = null;
		portChosen = null;
		playerChosen = null;
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
		this.add(localOrOnline);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		if (arg.equals("printBoard")) {
			printBoard(o);
		} else if (((String) arg).startsWith("gameOver")) {
			messageField.setText("Game over! The winner is: " + ((String) arg).split(" ")[1]);
		} else if (arg.equals("columnFull")) {
			errorField.setText("This column is full");
		} else if (arg.equals("badHost")) {
			errorField.setText("Host is niet goed...");
		} else if (arg.equals("cannotCreateClient")) {
			errorField.setText("Kan Client niet aanmaken...");
		}  else if (arg.equals("draw")) {
			messageField.setText("Game over! There is no winner, it is a draw...");
		} else if (((String) arg).startsWith("accepted")) {
			System.out.println("accepted!");
			this.remove(askName);
			label = new JLabel();
			label.setText("Looking for a game...");
			waitForGame.add(label);
			this.add(waitForGame);
			revalidate();
			repaint();
		} else if (((String) arg).startsWith("nameExists")) {
			errorField.setText("There already exists a player with this name on the server...");
		} else if (((String) arg).startsWith("gameStarted")) {
			//setup game panel
			this.setSize(800, 800);
			if (choosePlayers != null) {
				this.remove(choosePlayers);
			}
			setupBoard();
			printBoard(null);
		} else if (((String) arg).startsWith("message")) {
			String[] splitString = ((String) arg).split(" ", 3);
			String name = splitString[1];
			String message = splitString[2];
			chat.setText(chat.getText() + "\n" + name + " says: " + message);
		}
	}
	
	private void setupBoard() {
		try {
			boardImage = ImageIO.read(new File("board.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		boardLabel = new JLabel(new ImageIcon(boardImage));
		System.out.println("current label: " + boardLabel);
		boardLabel.addMouseListener(this);
	}
	
	private void printBoard(Observable o) {
		if (o == null) {
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
			homeMenu = new JButton("Leave");
			chat.setLineWrap(true);
			errorField = new JLabel();
			errorField.setForeground(Color.RED);
			playerName = new JLabel();
			sendChatMessage = new JButton("Send");
			sendChatMessage.addActionListener(this);
			chatMessage = new JTextField();
			hint = new JLabel();
			hint.setBounds(730, 510, 70, 10);
			chat.setBounds(20, 525, 640, 200);
			chat.setEditable(false);
			messageField.setBounds(330, 510, 400, 10);
			boardLabel.setBounds(20, 20, 640, 480);
			errorField.setBounds(20, 510, 300, 10);
			chatMessage.setBounds(40, 730, 300, 25);
			playerName.setBounds(5, 730, 50, 10);
			sendChatMessage.setBounds(350, 730, 80, 25);
			local.setBounds(450, 730, 75, 25);
			global.setBounds(550, 730, 75, 25);
			homeMenu.setBounds(670, 730, 100, 20);
			homeMenu.addActionListener(this);
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
			game.add(homeMenu);
			game.add(hint);
			this.add(game);
			revalidate();
			repaint();
		} else {
			errorField.setText("");
			Board board = ((Game) o).getBoard();
			Mark[][] field = board.getField();
			for (int hor = 0; hor < 7; hor++) {
				for (int ver = 0; ver < 6; ver++) {
					JLabel temp = null;
					if (field[ver][hor].equals(Mark.XX)) {
						temp = new JLabel(new ImageIcon(xXImage));
					} else if (field[ver][hor].equals(Mark.OO)) {
						temp = new JLabel(new ImageIcon(oOImage));
					}
					//draw mark
					if (temp != null) {
						temp.setBounds(35 + hor * 90, 26 + ver * 80, 70, 70);
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
		while (moveMade == -1) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		int temp = moveMade;
		moveMade = -1;
		askingMove = false;
		return temp;
	}

	public Player[] askForPlayers() {
		System.out.println("test");
		choosePlayers = new JLabel();
		this.remove(localOrOnline);
		JLabel name1 = new JLabel("Player 1: ");
		JLabel name2 = new JLabel("Player 2: ");
		player1Group = new ButtonGroup();
		player2Group = new ButtonGroup();
		human1 = new JRadioButton("Human");
		human2 = new JRadioButton("Human");
		aI1 = new JRadioButton("Computer");
		aI2 = new JRadioButton("Computer");
		human1.setSelected(true);
		human2.setSelected(true);
		difficultyLabel = new JLabel("Computer difficulty(1-4): ");
		JLabel difficultyLabel2 = new JLabel("Computer difficulty(1-4): ");
		difficultyText = new JTextField();
		difficultyText2 = new JTextField();
		player1Group.add(human1);
		player2Group.add(human2);
		player1Group.add(aI1);
		player2Group.add(aI2);
		submitPlayersButton = new JButton("Play!");
		player1 = new JTextField();
		player2 = new JTextField();
		name1.setBounds(20, 20, 100, 20);
		name2.setBounds(20, 60, 100, 20);
		player1.setBounds(80, 20, 100, 20);
		player2.setBounds(80, 60, 100, 20);
		human1.setBounds(180, 20, 100, 20);
		human2.setBounds(180, 60, 100, 20);
		aI1.setBounds(280, 20, 100, 20);
		aI2.setBounds(280, 60, 100, 20);
		submitPlayersButton.setBounds(20, 100, 100, 20);
		difficultyLabel.setBounds(20, 40, 150, 20);
		difficultyLabel2.setBounds(20, 80, 150, 20);
		difficultyText.setBounds(220, 40, 100, 20);
		difficultyText2.setBounds(220, 80, 100, 20);
		submitPlayersButton.addActionListener(this);
		choosePlayers.add(name1);
		choosePlayers.add(name2);
		choosePlayers.add(player1);
		choosePlayers.add(player2);
		choosePlayers.add(submitPlayersButton);
		choosePlayers.add(human1);
		choosePlayers.add(human2);
		choosePlayers.add(aI1);
		choosePlayers.add(aI2);
		choosePlayers.add(difficultyLabel);
		choosePlayers.add(difficultyLabel2);
		choosePlayers.add(difficultyText);
		choosePlayers.add(difficultyText2);
		this.add(choosePlayers);
		revalidate();
		repaint();
		
		while (playersChosen == null || playersChosen[0] == null || playersChosen[1] == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Player[] temp = playersChosen;
		return temp;
	}

	@Override
	public String askLocalOrOnline() {
		localOnline = null;
		localOrOnline.removeAll();
		this.remove(game);
		localBtn = new JButton("Local");
		onlineBtn = new JButton("Online");
		localBtn.addActionListener(this);
		onlineBtn.addActionListener(this);
		localOrOnline.add(localBtn);
		localOrOnline.add(onlineBtn);
		this.add(localOrOnline);
		revalidate();
		repaint();
		while (localOnline == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String temp = localOnline;
		localOnline = null;
		return temp;
	}

	@Override
	public String askPlayerName() {
		this.remove(askServer);
		askName.removeAll();
		JLabel labelz = new JLabel("Enter your name: ");
		txt = new JTextField();
		txt.setColumns(10);
		submitBtn = new JButton("Connect");
		difficultyLabel = new JLabel("Computer difficulty(1-4): ");
		difficultyText = new JTextField();
		difficultyText.setColumns(10);
		
		player1Group = new ButtonGroup();
		human1 = new JRadioButton("Human");
		aI1 = new JRadioButton("Computer");
		human1.setSelected(true);
		player1Group.add(human1);
		player1Group.add(aI1);
		submitBtn.addActionListener(this);
		askName.add(labelz);
		askName.add(txt);
		askName.add(human1);
		askName.add(aI1);
		askName.add(difficultyLabel);
		askName.add(difficultyText);
		askName.add(submitBtn);
		errorField = new JLabel();
		askName.add(errorField);
		this.add(askName);
		revalidate();
		repaint();
		System.out.println("name is not yet chosen");
		while (nameChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("name is chosen");
		String temp = nameChosen;
		nameChosen = null;
		return temp;
	}

	@Override
	public void setClient(Client clients) {
		this.client = clients;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(localBtn)) {
			localOnline = "local";
		} else if (e.getSource().equals(onlineBtn)) {
			localOnline = "online";
			System.out.println("online gekozen");
		} else if (e.getSource().equals(submitBtn)) {
			nameChosen = txt.getText();
			if (human1.isSelected()) {
				playerChosen = new HumanPlayer(txt.getText(), Mark.EMPTY);
			} else if (aI1.isSelected()) {
				int difficulty = Integer.parseInt(difficultyText.getText());
				playerChosen = new ComputerPlayer(txt.getText(), 
						Mark.EMPTY, new SmartStrategy(difficulty));
			}
		} else if (e.getSource().equals(sendChatMessage)) {
			String txts = chatMessage.getText();
			if (global.isSelected()) {
				System.out.println("GLOBAL SELECTED");
				client.sendMessage("chat_global " + txts);
			} else if (local.isSelected()) {
				client.sendMessage("chat_local " + txts);
			}
		} else if (e.getSource().equals(submitPlayersButton)) {
			String playerName1 = player1.getText();
			String playerName2 = player2.getText();
			playersChosen = new Player[2];
			if (human1.isSelected()) {
				playersChosen[0] = new HumanPlayer(playerName1, Mark.XX);
			} else if (aI1.isSelected()) {
				int dif = Integer.parseInt(difficultyText.getText());
				playersChosen[0] = new ComputerPlayer(playerName1, Mark.XX, new SmartStrategy(dif));
			}
			if (human2.isSelected()) {
				System.out.println(playerName2);
				playersChosen[1] = new HumanPlayer(playerName2, Mark.OO);
			} else if (aI2.isSelected()) {
				int dif = Integer.parseInt(difficultyText2.getText());
				playersChosen[1] = new ComputerPlayer(playerName2, Mark.OO, new SmartStrategy(dif));
			}
		} else if (e.getSource().equals(homeMenu)) {
			retry = true;
		} else if (e.getSource().equals(connectBtn)) {
			iPChosen = ipText.getText();
			portChosen = portText.getText();
		}
	}
	
	public void checkRetry() {
		if (retry) {
			retry = false;
			this.remove(game);
			game.removeAll();
			ConnectFour.shutdown();
			new ConnectFour(this);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getSource().equals(boardLabel)) {
			//clicked on board
			int clickX = e.getX();
			if (askingMove) {
				if (clickX >= 0 && clickX < 95) {
					//row 1
					moveMade = 0;
				} else if (clickX >= 95 && clickX < 185) {
					//row 2
					moveMade = 1;
				} else if (clickX >= 185 && clickX < 275) {
					//row 2
					moveMade = 2;
				} else if (clickX >= 275 && clickX < 365) {
					//row 3
					moveMade = 3;
				} else if (clickX >= 365 && clickX < 455) {
					//row 4
					moveMade = 4;
				} else if (clickX >= 455 && clickX < 545) {
					//row 5
					moveMade = 5;
				} else if (clickX >= 545 && clickX < 640) {
					//row 6
					moveMade = 6;
				}
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) { };

	@Override
	public void mouseReleased(MouseEvent e) { };

	@Override
	public void mouseEntered(MouseEvent e) { };

	@Override
	public void mouseExited(MouseEvent e) { };

	public void giveHint(int i) {
		hint.setText("Hint: column " + (i + 1));
	}

	@Override
	public String askIPAdress() {
		askServer = new JPanel();
		this.remove(localOrOnline);
		askName.removeAll();
		JLabel labelIP = new JLabel("IP adress: ");
		JLabel labelPort = new JLabel("Port: ");
		txt = new JTextField();
		txt.setColumns(10);
		connectBtn = new JButton("Connect");
		ipText = new JTextField();
		portText = new JTextField();
		ipText.setColumns(28);
		portText.setColumns(28);
		
		connectBtn.addActionListener(this);
		askServer.add(labelIP);
		askServer.add(ipText);
		askServer.add(labelPort);
		askServer.add(portText);
		askServer.add(connectBtn);
		errorField = new JLabel();
		askServer.add(errorField);
		this.add(askServer);
		revalidate();
		repaint();
		while (iPChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String temp = iPChosen;
		iPChosen = null;
		return temp;
	}

	@Override
	public String askPort() {
		while (portChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		String temp = portChosen;
		portChosen = null;
		return temp;
	}

	@Override
	public Player askType(String name) {
		while (playerChosen == null) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Player temp = playerChosen;
		playerChosen = null;
		return temp;
	}

	@Override
	public void disconnectedError() {
		errorField.setText("Connection error...");
	}
}
