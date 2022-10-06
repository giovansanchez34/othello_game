package othello_players;

import gamelogic.Pair;
import gamelogic.OthelloGameState;
import java.util.Scanner;

/**
 * A player that is capable of playing Othello through the console in which they are prompted to enter their move
 * via the keyboard.
 */
public class OthelloConsolePlayer implements OthelloPlayer
{
    // used to prompt the player to make a move
    private final Scanner inputReader;

    /**
     * Sets up the necessary tools needed for the player to play othello.
     */
    public OthelloConsolePlayer()
    {
        inputReader = new Scanner(System.in);
    }


    /**
     * Prompts the player to enter a move via the keyboard and returns the coordinate (ROW, COL) of that move.
     */
    @Override
    public Pair<Integer, Integer> chooseMove(OthelloGameState gameState)
    {
        int row = readNumber("What row would you like to place your token on? (Enter a number from 0-7) ");
        clearBuffer();
        int col = readNumber("What column would you like to place your token on? (Enter a number from 0-7) ");
        return new Pair<>(row, col);
    }


    /**
     * Prompts to user enter an integer based on the given message that would be displayed on the console. If the player
     * does not enter an integer, it would continuously prompt until they do.
     */
    private int readNumber(String message)
    {
        while (true)
        {
            System.out.print(message);
            if (inputReader.hasNextInt())
            {
                return inputReader.nextInt();
            }
            System.out.println("Given input was not an integer. Please try again.");
            clearBuffer();
        }
    }

    /**
     * Clears the buffer to prevent messing up other inputs the user would enter as the game progresses.
     */
    private void clearBuffer()
    {
        inputReader.nextLine();
    }

}