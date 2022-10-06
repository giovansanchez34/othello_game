package othello_players;

import gamelogic.OthelloGameState;
import gamelogic.Pair;

/**
 * A player that is capable of playing Othello, regardless through a GUI or a UI using
 * the console. It is implemented through other new classes that implement its functionality.
 */
public interface OthelloPlayer
{
    /**
     * Prompts the current player to choose a move based on the given
     * othello game state, returning a coordinate (ROW, COL) of a tile
     * in which they would like to place their token on.
     */
    Pair<Integer, Integer> chooseMove(OthelloGameState gameState);
}