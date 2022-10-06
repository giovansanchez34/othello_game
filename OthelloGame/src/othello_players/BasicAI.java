package othello_players;

import gamelogic.Pair;
import gamelogic.OthelloGameState;
import gamelogic.OthelloException;
import gamelogic.Tile;
import java.util.function.BiFunction;


/**
 * An AI that is capable of playing an othello game using the minimax algorithm to look three moves ahead to ultimately
 * choose a move that would increase its chances of winning.
 */
public class BasicAI implements OthelloAI
{

    /**
     * chooseMove() takes in the current state of the othello game and chooses
     * the move that appears to be best in its current position.
     */
    public Pair<Integer, Integer> chooseMove(OthelloGameState gameState)
    {
        // starts off really small since a move hasn't been determined yet
        double bestScore = Double.NEGATIVE_INFINITY;
        // defaults to (0, 0) since AI hasn't determined move yet
        Pair<Integer, Integer> bestMove = new Pair<>(0, 0);
        // will only look three moves ahead to determine the best move to make.
        int depth = 3;
        // determines what token the current AI has.
        boolean isBlack = gameState.isBlackTurn();

        for (Tile tile : gameState.board())
        {
            try
            {
                if (gameState.isValidMove(tile.row(), tile.col()))
                {
                    OthelloGameState clonedGame = (OthelloGameState) gameState.clone();
                    clonedGame.makeMove(tile.row(), tile.col());
                    double score = chooseBestMove(clonedGame, depth - 1, isBlack);

                    if (score > bestScore)
                    {
                        bestScore = score;
                        bestMove = new Pair<>(tile.row(), tile.col());
                    }
                }
            }
            catch (OthelloException ignored) {}
        }
        return bestMove;
    }

    /**
     * chooseBestMove() takes in the current state of a othello game and tries out all possible moves
     * that current player can make. Each moves is assigned a score which will help determine
     * the best move for the AI to take. If its the current AI's turn, it will try to maximize the
     * the chances of the AI winning the overall game with the aid of a determiner function that helps maximize the best
     * score. Otherwise, it will try to minimize the chances of the opponent winning the overall in which it assumes
     * the opponent will play an optimal move with the aid of a determiner function that helps minimize the opponent's score.
     */
    private double chooseBestMove(OthelloGameState gameState, int depth, boolean isBlack)
    {
        if (gameState.isGameOver() || depth == 0)
        {
            return evaluate(gameState, isBlack);
        }

        double bestScore = (isAITurn(gameState, isBlack) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
        BiFunction<Double, Double, Double> determiner = (isAITurn(gameState, isBlack) ? Math::max : Math::min);

        for (Tile tile : gameState.board())
        {
            try
            {
                if (gameState.isValidMove(tile.row(), tile.col()))
                {
                    OthelloGameState clonedGame = (OthelloGameState) gameState.clone();
                    clonedGame.makeMove(tile.row(), tile.col());
                    bestScore = determiner.apply(bestScore, chooseBestMove(clonedGame, depth - 1, isBlack));
                }
            }
            catch (OthelloException ignored) {}
        }
        return bestScore;
    }




    /**
     * Evaluates the board and returns a score to determine whether this best scenario the AI could attempt to take.
     * Here, it uses the difference between the number of tokens the AI and the opponent have on the board. A positive
     * score indicates that the hypothetical game state gives the AI an advantage and a negative score indicates that
     * the hypothetical game state gives the opponent an advantage.
     */
    private double evaluate(OthelloGameState gameState, boolean isBlack)
    {
        if (isBlack)
        {
            return gameState.blackScore() - gameState.whiteScore();
        }
        else
        {
            return gameState.whiteScore() - gameState.blackScore();
        }
    }
}