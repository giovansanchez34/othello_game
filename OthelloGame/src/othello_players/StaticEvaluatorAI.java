package othello_players;

import gamelogic.OthelloException;
import gamelogic.Pair;
import gamelogic.OthelloGameState;
import gamelogic.OthelloBoard;
import gamelogic.Tile;
import gamelogic.OthelloTileState;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.BiFunction;


/**
 * This AI chooses a move that appears to be
 * the best, trying to increase its chances of winning the game. In addition, its evaluation is static which
 * means that no matter what "stage" the game is currently in, the same heuristics will be called for every evaluation
 */
public class StaticEvaluatorAI implements OthelloAI
{
    /**
     * chooseMove() takes in the current state of the othello game and chooses
     * the move that appears to be best in its current position.
     */
    public Pair<Integer, Integer> chooseMove(OthelloGameState gameState)
    {
        // defaults to (0, 0) since AI hasn't determined move yet
        Pair<Integer, Integer> bestMove = new Pair<>(0, 0);
        // starts off really small since a move hasn't been determined yet
        double bestScore = Double.NEGATIVE_INFINITY;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        // will only look three moves ahead to determine the best move to make.
        int depth = 4;
        // determines what token the current AI has.
        boolean isBlack = gameState.isBlackTurn();

        for (Tile tile : gameState.board())
        {
            if (gameState.isValidMove(tile.row(), tile.col()))
            {
                OthelloGameState clonedGame = (OthelloGameState) gameState.clone();
                this.makeMove(clonedGame, tile.row(), tile.col());
                double score = chooseBestMove(clonedGame, depth - 1, isBlack, alpha, beta);
                alpha = Math.max(alpha, score);
                if (score > bestScore)
                {
                    bestScore = score;
                    bestMove = new Pair<>(tile.row(), tile.col());
                }
            }
        }
        return bestMove;
    }


    /**
     * Takes in the current state of a othello game and tries out all possible moves
     * that current player can make. Each moves is assigned a score which will help determine
     * the best move for the AI to take. If its the current AI's turn, it will try to maximize the
     * the chances of the AI winning the overall game. Otherwise, it will try to minimize the chances
     * of the opponent winning the overall in which it assumes the opponent will play an optimal move.
     * alpha and beta help make the search much more efficient since it stops searching a game state when
     * there already exists a better move.
     */
    private double chooseBestMove(OthelloGameState gameState, int depth, boolean isBlack, double alpha, double beta)
    {
        if (gameState.isGameOver() || depth == 0)
        {
            return evaluate(gameState, isBlack);
        }

        boolean isMaximizing = isAITurn(gameState, isBlack);
        BiFunction<Double, Double, Double> determiner = (isMaximizing ? Math::max : Math::min);
        double bestScore = (isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);

        for (Tile tile : gameState.board())
        {
            if (gameState.isValidMove(tile.row(), tile.col()))
            {
                OthelloGameState clonedGame = (OthelloGameState) gameState.clone();
                this.makeMove(clonedGame, tile.row(), tile.col());
                bestScore = determiner.apply(bestScore, chooseBestMove(clonedGame, depth - 1, isBlack, alpha, beta));

                alpha = updateAlpha(alpha, bestScore, isMaximizing);
                beta = updateBeta(beta, bestScore, isMaximizing);
                if (beta <= alpha)
                {
                    // a better move already exists thus we end the search early for this game state
                    return bestScore;
                }
            }
        }
        return bestScore;
    }



    /**
     * Returns and updates the alpha score if it is currently maximizing the best move for the AI. Otherwise, it just returns the same
     * score.
     */
    private double updateAlpha(double alpha, double bestScore, boolean isMaximizing)
    {
        return (isMaximizing ? Math.max(alpha, bestScore) : alpha);
    }


    /**
     * Returns and updates the beta score if it is currently minimizing a move for the AI. Otherwise, it just returns the
     * same score.
     */
    private double updateBeta(double beta, double bestScore, boolean isMaximizing)
    {
        return (!isMaximizing ? Math.min(beta, bestScore) : beta);
    }


    /**
     * evaluates the state of the given othello game and returns a score
     *  to determine how advantageous this state is for the current player.
     */
    private double evaluate(OthelloGameState gameState, boolean isBlack)
    {
        OthelloTileState tokenColor = (isBlack ? OthelloTileState.BLACK : OthelloTileState.WHITE);
        return 10 * evaluateDiscParity(gameState, isBlack)
                + 20 * evaluateActualMobility(gameState, tokenColor)
                + 15 * evaluatePotentialMobility(gameState, tokenColor)
                + 1000 * evaluateCornersCaptured(gameState, tokenColor)
                + 1000 * evaluateEdgeStability(gameState, tokenColor);
    }


    /**
     * evaluates the state of the given othello game and returns the number of discs
     * that the given player has on the board.
     */
    private double evaluateDiscParity(OthelloGameState gameState, boolean isBlack)
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


    /**
     * Returns the number of corners captured by the given player in the given othello board.
     */
    private int countCornersCaptured(OthelloBoard board, OthelloTileState tokenColor)
    {
        return (int) CORNERS.stream()
                .filter((corner)-> board.tileStateAt(corner.first, corner.second) == tokenColor)
                .count();
    }

    /**
     * evaluates the state of the given othello game and returns the difference of how many corners were captured
     * between the two players. A positive score indicates that the current player  will have an advantage. A negative
     * score indicates that the current player will have an disadvantage.
     */
    private int evaluateCornersCaptured(OthelloGameState gameState, OthelloTileState tokenColor)
    {
        OthelloBoard board = gameState.board();
        OthelloTileState opponentColor = (tokenColor == OthelloTileState.BLACK ? OthelloTileState.WHITE : OthelloTileState.BLACK);
        int othelloAICorners = countCornersCaptured(board, tokenColor);
        int opponentCorners = countCornersCaptured(board, opponentColor);
        return othelloAICorners - opponentCorners;
    }


    /**
     * Returns true if the given coordinate (ROW, COL) on the board is legal move that the player can make in the current state in the game.
     * To determine if it is a legal move, the given coordinate (ROW, COL) must be an empty cell and it must find
     * a disc of the player's color that is at the far end of the opponent's row of discs
     * dx and dy determines which direction we are looking at to find the player's disc
     */
    private boolean isLegalMove(OthelloBoard board, int row, int col, OthelloTileState tokenColor,
                     OthelloTileState opponentColor, int dx, int dy)
    {
        if (!board.isValidCoordinate(row, col) || board.tileStateAt(row, col) != opponentColor)
        {
            return false;
        }

        while (board.isValidCoordinate(row, col))
        {
            if (board.tileStateAt(row, col) == tokenColor)
            {
                // we found the player's disc which means this is a legal move
                return true;
            }
            else if (board.tileStateAt(row, col) == OthelloTileState.EMPTY)
            {
                // the player's disc has not been found which means this is not a legal move
                return false;
            }
            row += dx; // either goes left or right depending on the given delta
            col += dy; // either goes down or up depending on the given delta
        }
        return false;
    }

    /**
     *  Returns true if the player can make a move at the given coordinate (ROW, COL) and
     *  false otherwise. Looks at all possible 8 directions of a cell.
     */
    private boolean isValidMoveAt(OthelloBoard board, int row, int col, OthelloTileState tokenColor, OthelloTileState opponentColor)
    {
        if (board.tileStateAt(row, col) == OthelloTileState.EMPTY)
        {
            return    isLegalMove(board, row - 1, col, tokenColor, opponentColor, -1, 0)
                    || isLegalMove(board, row + 1, col, tokenColor, opponentColor, 1, 0)
                    || isLegalMove(board, row, col + 1, tokenColor, opponentColor, 0, 1)
                    || isLegalMove(board, row, col - 1, tokenColor, opponentColor, 0, -1)
                    || isLegalMove(board, row + 1, col - 1, tokenColor, opponentColor, 1, -1)
                    || isLegalMove(board, row - 1, col - 1, tokenColor, opponentColor, -1, -1)
                    || isLegalMove(board, row + 1, col + 1, tokenColor, opponentColor, 1, 1)
                    || isLegalMove(board, row - 1, col + 1, tokenColor, opponentColor, -1, 1);
        }
        return false;
    }


    /**
     * Returns the total possible moves that the current player can make in the current Othello Game.
     */
    private int countPlayerMobility(OthelloGameState gameState, OthelloTileState tokenColor, OthelloTileState opponentColor)
    {
        OthelloBoard board = gameState.board();
        int totalMoves = 0;
        for (Tile tile : board)
        {
            if (isValidMoveAt(board, tile.row(), tile.col(), tokenColor, opponentColor))
            {
                ++totalMoves;
            }
        }
        return totalMoves;
    }

    /**
     * evaluates the given state of the othello game and returns the difference of the number of moves each player can
     * make in the current game. A positive score indicates that the current player will have an advantage. A negative
     * score indicates that the current player will have an disadvantage.
     */
    private int evaluateActualMobility(OthelloGameState gameState, OthelloTileState tokenColor)
    {
        OthelloTileState opponentColor = (tokenColor == OthelloTileState.BLACK ? OthelloTileState.WHITE : OthelloTileState.BLACK);
        int othelloAIMobility = countPlayerMobility(gameState, tokenColor, opponentColor);
        int opponentMobility = countPlayerMobility(gameState, opponentColor, tokenColor);
        return othelloAIMobility - opponentMobility;
    }

    /**
     * Returns true if given (ROW, COL) coordinate has an opponent disc. Otherwise, it returns false
     */
    private boolean isOpponentDisc(OthelloBoard board, int row, int col, OthelloTileState opponentColor)
    {
        // checks if (ROW,COL) is a valid tile first before checking what contents is on the cell
        return board.isValidCoordinate(row, col) && board.tileStateAt(row, col) == opponentColor;
    }


    /**
     * Returns true if the given (x, y) coordinate is a possible move that the
     * current player can make in their next turn. To be consider a potential move, the cell has to be empty
     * has to be next to an opponent's disc in either of the 8 possible directions.
     */
    private boolean isValidPotentialMoveAt(OthelloBoard board, int row, int col, OthelloTileState opponentColor)
    {
        if (board.tileStateAt(row, col) == OthelloTileState.EMPTY)
        {
            return   isOpponentDisc(board, row - 1, col, opponentColor)
                    || isOpponentDisc(board, row + 1, col, opponentColor)
                    || isOpponentDisc(board, row, col + 1, opponentColor)
                    || isOpponentDisc(board, row, col + 1, opponentColor)
                    || isOpponentDisc(board, row, col - 1, opponentColor)
                    || isOpponentDisc(board, row + 1, col + 1, opponentColor)
                    || isOpponentDisc(board, row - 1, col - 1, opponentColor)
                    || isOpponentDisc(board, row - 1, col + 1, opponentColor)
                    || isOpponentDisc(board, row + 1, col - 1, opponentColor);
        }
        return false;
    }

    /**
     * Returns the number of possible moves that the current player can make in the current game.
     */
    private int countPlayerPotentialMobility(OthelloGameState gameState, OthelloTileState opponentColor)
    {
        int potentialMoves = 0;
        OthelloBoard board = gameState.board();
        for (Tile tile : board)
        {
            if (isValidPotentialMoveAt(board, tile.row(), tile.col(), opponentColor))
            {
                ++potentialMoves;
            }
        }
        return potentialMoves;
    }

    /**
     * Evaluates the current state of the othello game and returns the difference
     * between the number of possible moves each player can make in their next turn. A positive score indicates that
     * the current player will have an advantage. A negative score indicates that the current player
     * will have an disadvantage.
     */
    private int evaluatePotentialMobility(OthelloGameState gameState, OthelloTileState tokenColor)
    {
        OthelloTileState opponentColor = (tokenColor == OthelloTileState.BLACK ? OthelloTileState.WHITE : OthelloTileState.BLACK);
        int othelloAIMobility = countPlayerPotentialMobility(gameState, opponentColor);
        int opponentMobility = countPlayerPotentialMobility(gameState, tokenColor);
        return othelloAIMobility - opponentMobility;
    }


    /**
     * Finds the coordinates of all the player's discs that are near a corner
     * in which the player has a disc on.
     */
    private void findEdgeDiscs(OthelloBoard board, OthelloTileState tokenColor, HashSet<Pair<Integer, Integer>> edgeDiscs,
                               int row, int col, int dx, int dy)
    {
        while (board.isValidCoordinate(row, col))
        {
            Pair<Integer, Integer> coordinate = new Pair<>(row, col);

            if (board.tileStateAt(row, col) != tokenColor || edgeDiscs.contains(coordinate))
            {
                // stops searching when we don't find the player's discs along the edge or
                // avoids searching in a direction it already has searched
                break;
            }
            edgeDiscs.add(coordinate);
            row += dx; // either moves left or right depending on the delta
            col += dy; // either moves up or down depending on the delta
        }
    }


    /**
     * Returns the number of discs that the player has along the corners and the adjacent discs that are close to the
     * corner. It looks in only 4 directions instead of 8 since looking at the diagonals can be risky.
     */
    private int edgeStability(OthelloGameState gameState, OthelloTileState tokenColor)
    {
        OthelloBoard board = gameState.board();
        HashSet<Pair<Integer, Integer>> edgeDiscs = new HashSet<>();
        // always starts off searching in the corners when looking for Edge discs.
        findEdgeDiscs(board, tokenColor, edgeDiscs, 0, 0, 1, 0);
        findEdgeDiscs(board, tokenColor, edgeDiscs, 0, 0, 0, 1);
        findEdgeDiscs(board, tokenColor, edgeDiscs, board.rows() - 1, 0, -1, 0);
        findEdgeDiscs(board, tokenColor, edgeDiscs, board.rows() - 1, 0, 0, 1);
        findEdgeDiscs(board, tokenColor, edgeDiscs, 0, board.cols() - 1, 0, -1);
        findEdgeDiscs(board, tokenColor, edgeDiscs, 0, board.cols() - 1, 1, 0);
        findEdgeDiscs(board, tokenColor, edgeDiscs, board.rows() - 1, board.cols() - 1, 0, -1);
        findEdgeDiscs(board, tokenColor, edgeDiscs, board.rows() - 1, board.cols() - 1, -1, 0);
        return edgeDiscs.size();
    }


    /**
     * Evaluates the state of the given othello game and returns the difference
     * of the number of edge discs each player has. A positive score indicates that
     * the current player will have an advantage. A negative score indicates that the current player
     * will have an disadvantage.
     */
    private int evaluateEdgeStability(OthelloGameState gameState, OthelloTileState tokenColor)
    {
        OthelloTileState opponentColor = (tokenColor == OthelloTileState.BLACK ? OthelloTileState.WHITE : OthelloTileState.BLACK);
        int othelloAIEdgeStability = edgeStability(gameState, tokenColor);
        int opponentStability = edgeStability(gameState, opponentColor);
        return othelloAIEdgeStability - opponentStability;
    }

}
