package othello_players;

import gamelogic.OthelloGameState;
import gamelogic.OthelloException;
import gamelogic.Pair;
import java.util.ArrayList;
import java.util.List;

/**
 * Any AI's that is added to Othello must implement this interface to distinguish it from non-AI players for the GUI.
 */
public interface OthelloAI extends OthelloPlayer
{
    // coordinates to the corners of the 8 x 8 othello board
    static final List<Pair<Integer, Integer>> CORNERS = new ArrayList<>(List.of(new Pair<>(0, 0), new Pair<>(0, 7), new Pair<>(7, 0), new Pair<>(7, 7)));

    /**
     * Returns true if it's the current AI's turn to make a move.
     */
    default boolean isAITurn(OthelloGameState gameState, boolean isBlack)
    {
        return (isBlack? gameState.isBlackTurn() : gameState.isWhiteTurn());
    }

    /**
     * Makes a move at (ROW, COL) for the given othello game state.
     */
    default void makeMove(OthelloGameState gameState, int row, int col)
    {
        try
        {
            gameState.makeMove(row, col);
        }
        catch (OthelloException ignored) {}
    }
}
