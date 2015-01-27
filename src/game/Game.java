package game;

import players.Player;

public interface Game {
    public Board getBoard();
    
    public Player getWinner();
}
