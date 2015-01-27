package game;

import network.Client;
import players.Player;
import views.GUI;
import views.TUI;
import views.View;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;


public class ConnectFour extends Observable{
    static Thread[] threads;
    
    public ConnectFour() {
        View view;
        threads = new Thread[2];
        String ui = View.askWhichUI();
        switch (ui) {
        case "TUI":
            view = new TUI();
            break;
        case "GUI":
            view = new GUI();
            break;
        default:
            view = null;
            break;
        }
        this.addObserver(view);
        
        boolean done = false;
        while (!done) {
            String gameType = view.askLocalOrOnline();
            if (gameType.equals("local")) {
                Player[] players = view.askForPlayers();
                this.setChanged();
                this.notifyObservers("gameStarted");
                this.createNewGame(players, view);
                done = true;
            } else if (gameType.equals("online")) {
                InetAddress host = null;
                try {
                    host = InetAddress.getByName("spitfire.student.utwente.nl");
                } catch (UnknownHostException e) {
                    this.setChanged();
                    this.notifyObservers("badHost");
                }
                
                try {
                    threads[0] = new Thread(new Client(host, 2003, view));
                    threads[0].start();
                    System.out.println("komt langs");
                    done = true;
                } catch (IOException e) {
                    this.setChanged();
                    this.notifyObservers("cannotCreateClient");
                }
            }
        }
        this.deleteObserver(view);
    }
    
    public ConnectFour(View view) {
        this.addObserver(view);
        boolean done = false;
        while (!done) {
            System.out.println("in loop");
            String gameType = view.askLocalOrOnline();
            System.out.println("ask");
            if (gameType.equals("local")) {
                Player[] players = view.askForPlayers();
                this.setChanged();
                this.notifyObservers("gameStarted");
                this.createNewGame(players, view);
                done = true;
            } else if (gameType.equals("online")) {
                InetAddress host = null;
                try {
                    host = InetAddress.getByName("spitfire.student.utwente.nl");
                } catch (UnknownHostException e) {
                    this.setChanged();
                    this.notifyObservers("badHost");
                }
                
                try {
                    threads[1] = new Thread(new Client(host, 2003, view));
                    threads[1].start();
                    done = true;
                } catch (IOException e) {
                    this.setChanged();
                    this.notifyObservers("cannotCreateClient");
                }
            }
        }
    }
    
    public static void main(String[] args) {
        new ConnectFour();
    }
    
    public void createNewGame(Player[] players, View view) {
        new NormalGame(players[0], players[1], view);
    }
    
    public static void shutdown() {
        for (Thread t: threads) {
            if (t != null) {
                t.interrupt();
                t = null;
            }
        }
    }
}
