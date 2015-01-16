import java.util.Scanner;

public class HumanPlayer extends Player {

    public HumanPlayer(String name, Mark mark) {
        super(name, mark);
    }
    
    public int determineMove(Board board) {
        String prompt = "> " + getName() + " (" + getMark().toString() + ")"
                + ", what is your choice? ";
        int choice = readInt(prompt);
        boolean valid = board.isField(choice) && board.isEmptyField(choice);
        while (!valid) {
            System.out.println("ERROR: field " + choice
                    + " is no valid choice.");
            choice = readInt(prompt);
            valid = board.isField(choice) && board.isEmptyField(choice);
        }
        return choice;
    }
    
    private int readInt(String prompt) {
        int value = 0;
        boolean intRead = false;
        do {
            System.out.print(prompt);
            String line = (new Scanner(System.in)).nextLine();
            Scanner scannerLine = new Scanner(line);
            if (scannerLine.hasNextInt()) {
                intRead = true;
                value = scannerLine.nextInt();
            }
        } while (!intRead);

        return value;
    }

}
