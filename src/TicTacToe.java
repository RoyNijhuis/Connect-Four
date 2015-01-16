

public class TicTacToe {
	public static void main(String args[])
	{
		int currentPlayer = 0;
		Player[] players = new Player[2];
		for(int i=0;i<args.length;i++)
		{
				if(currentPlayer == 0)
				{
					players[i] = new HumanPlayer(args[currentPlayer], Mark.XX);
				}
				else
				{
					players[i] = new HumanPlayer(args[currentPlayer], Mark.OO);
				}
			currentPlayer++;
		}
		Game game = new Game(players[0], players[1]);
	}
}
