package othello_players;

import gamelogic.OthelloGameState;
import gamelogic.Pair;
import gamelogic.Tile;
import java.util.ArrayList;
import java.util.Random;




/**
 * A RandomAI has an unpredictable behavior. It chooses any available moves it can make at its current position at
 * random without the use of any heuristics.
 */
public class RandomAI implements OthelloAI
{
    /**
     * Returns a random coordinate (ROW, COL) of a tile on the othello board in which the AI would like to place
     * their token on.
     */
    public Pair<Integer, Integer> chooseMove(OthelloGameState gameState)
    {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        for (Tile tile : gameState.board())
        {
            if (gameState.isValidMove(tile.row(), tile.col()))
            {
                moves.add(new Pair<>(tile.row(), tile.col()));
            }
        }
        return chooseRandomMove(moves);
    }

    /**
     * Returns a random move from the given list of available moves.
     */
    private Pair<Integer, Integer> chooseRandomMove(ArrayList<Pair<Integer, Integer>> moves)
    {
        Random randomizer = new Random();
        // choose a random index from 0 to the size of the list
        int moveIndex = randomizer.nextInt(moves.size());
        return moves.get(moveIndex);
    }
}
