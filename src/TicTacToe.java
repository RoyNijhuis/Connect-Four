

public class TicTacToe {
	public static void main(String args[])
	{
		int currentPlayer = 0;
		Player[] players = new Player[2];
		for(int i=0;i<args.length;i++)
		{
			if(args[i].equals("-N"))
			{
				if(currentPlayer == 0)
				{
					players[i] = new ComputerPlayer(Mark.XX, new NaiveStrategy());
				}
				else
				{
					players[i] = new ComputerPlayer(Mark.OO, new NaiveStrategy());
				}
			}
			else if(args[i].equals("-S"))
			{
				if(currentPlayer == 0)
				{
					players[i] = new ComputerPlayer(Mark.XX, new SmartStrategy());
				}
				else
				{
					players[i] = new ComputerPlayer(Mark.OO, new SmartStrategy());
				}
			}
			else
			{
				if(currentPlayer == 0)
				{
					players[i] = new HumanPlayer(args[currentPlayer], Mark.XX);
				}
				else
				{
					players[i] = new HumanPlayer(args[currentPlayer], Mark.OO);
				}
			}
			currentPlayer++;
		}
		Game game = new Game(players[0], players[1]);
		game.start();
	}
}
