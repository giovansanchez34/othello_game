package OthelloUI;

import gamelogic.OthelloGameState;
import gamelogic.Pair;
import gamelogic.OthelloException;
import othello_players.OthelloPlayer;
import othello_players.OthelloConsolePlayer;
import othello_players.BasicAI;
import java.util.Scanner;


/**
 * OthelloGameUI is an interactive UI that allows players to play a game of Othello via the console.
 * NOTE: This console UI is used as a rough draft for the actual GUI
 */
public class OthelloConsoleUI
{
    private OthelloGameState gameState;
    // keeps the user interface running until the user quits
    private boolean running;

    /**
     * Sets up the underlying details needed to run the UI.
     */
    public OthelloConsoleUI()
    {
        running = true;
        setNewGame();
    }


    /**
     * Runs the Othello UI.
     */
    public void run()
    {
        System.out.println("Welcome to Othello!");
        while (running)
        {
            runGame();
            if (playAgain())
            {
                setNewGame();
            }
            else
            {
                running = false;
            }
        }
    }


    /**
     * Starts running a game of Othello
     */
    private void runGame()
    {
        Pair<OthelloPlayer, OthelloPlayer> players = getPlayers();
        OthelloPlayer blackPlayer = players.first;
        OthelloPlayer whitePlayer = players.second;

        while (!gameState.isGameOver())
        {
            System.out.print(gameState.board());
            System.out.println(gameState.isBlackTurn() ? "BLACK'S TURN" : "WHITE'S TURN");
            Pair<Integer, Integer> move = getPlayerMove(gameState.isBlackTurn()? blackPlayer : whitePlayer);

            try
            {
                gameState.makeMove(move.first, move.second);
            }
            catch (OthelloException exc)
            {
                System.out.println("\n" + exc.getMessage() + "\nPlease try again.");
            }
        }

        displayGameResults();
    }

    /**
     * Displays the final results of the othello game: who won, the score of each player, and the final state of the board.
     */
    private void displayGameResults()
    {
        System.out.println(gameState.board());
        String winner = winner();
        String score = finalScore();
        System.out.println(winner + "\n" + score);

    }

    /**
     * Determines which player won the game or if the game ended in a draw.
     */
    private String winner()
    {
        if (gameState.blackScore() > gameState.whiteScore())
        {
            return "BLACK PLAYER WINS";
        }

        else if (gameState.whiteScore() > gameState.blackScore())
        {
            return "WHITE PLAYER WINS";
        }

        else
        {
            return "DRAW";
        }
    }

    /**
     * Returns the score of each player at the end of the game.
     */
    private String finalScore()
    {
        return "Black Player: " + gameState.blackScore() + " points\nWhite Player: " + gameState.whiteScore() + " points";
    }


    /**
     * Prompts the player to make a move
     */
    private Pair<Integer, Integer> getPlayerMove(OthelloPlayer player)
    {
        return player.chooseMove(gameState);
    }

    /**
     * Returns true if the user wants to play a new game of Othello and false otherwise.
     */
    private boolean playAgain()
    {
        Scanner inputReader = new Scanner(System.in);
        while (true)
        {
            System.out.print("Do you want to play again? (Enter 'Y' for yes or 'N' for no) ");
            String response = inputReader.nextLine().toLowerCase();
            if (response.equals("y"))
            {
                return true;
            }
            else if (response.equals("n"))
            {
                return false;
            }
            else
            {
                System.out.println("Please enter a valid input ('Y' or 'N'). Try again.");
            }
        }
    }

    /**
     * Starts up a new game of othello that uses an 8x8 board by "resetting" the state of the game.
     */
    private void setNewGame()
    {
        gameState = new OthelloGameState();
    }

    /**
     * Returns the set of players that will play in this game of othello. Asks the user for which color token he would
     * like to play with and the type of player he would like to play against.
     */
    private Pair<OthelloPlayer, OthelloPlayer> getPlayers()
    {
        String playerTokenColor = getPlayerColor();
        OthelloPlayer blackPlayer, whitePlayer;

        if (playerTokenColor.equals("b"))
        {
            blackPlayer = new OthelloConsolePlayer();
            whitePlayer = getOpponentType();
        }

        else
        {
            whitePlayer = new OthelloConsolePlayer();
            blackPlayer = getOpponentType();
        }
        return new Pair<>(blackPlayer, whitePlayer);
    }

    /**
     * Asks the player if he wants to play with white or black tokens. It will continuously prompt the user until they enter
     * a valid response
     */
    private String getPlayerColor()
    {
        Scanner inputReader = new Scanner(System.in);
        while (true)
        {
            System.out.print("What color token do you want to play as? (Enter 'B' for black or 'W' for white) ");
            String response = inputReader.nextLine().toLowerCase();

            if (response.equals("b") || response.equals("w"))
            {
                return response;
            }
            System.out.println("You can only enter 'B' for black or 'W' for white. Please try again.");
        }
    }

    /**
     * Asks the player if he wants to play with an AI or another regular player. It then returns the proper type of othello
     * player based on the player's response. If the player failed to enter a valid response, it will continuously prompt
     * them.
     */
    private OthelloPlayer getOpponentType()
    {
        Scanner inputReader = new Scanner(System.in);
        while (true)
        {
            System.out.print("Do you want to play against an AI or another regular player? (Enter 'y' for AI or 'n' for regular player) ");
            String response = inputReader.nextLine().toLowerCase();

            if (response.equals("y"))
            {
                return new BasicAI();
            }

            else if (response.equals("n"))
            {
                return new OthelloConsolePlayer();
            }
            System.out.println("You can only enter 'Y' for AI or 'n' for regular player. Please try again.");
        }
    }



}