package othello_players;

import gamelogic.Pair;
import gamelogic.OthelloGameState;

/**
 * An Othello player used to represent a human playing othello on the GUI.
 */
public class HumanPlayer implements OthelloPlayer
{
    /**
     * Automatically returns (0,0) since this player will be making his/her move by interacting with the GUI.
     */
    @Override
    public Pair<Integer, Integer> chooseMove(OthelloGameState gameState)
    {
        return new Pair<>(0, 0);
    }
}
