package gamelogic;

import java.util.ArrayList;


/**
 * OthelloGameState represents the current game state of an othello game and the underlying
 * logic of Othello. It keeps track of important details that occur during the game.
 */
public class OthelloGameState implements Cloneable
{
    // keeps track of how many points each player has in a game
    private int _blackScore;
    private int _whiteScore;
    // keeps track of whether or not the game is finished
    private boolean gameOver;
    // keeps track of who's turn it is
    private boolean blackTurn;
    // keeps track of the current state of the game
    private OthelloBoard gameBoard;
    // keeps track of the tiles in which the current player can place a token to outflank the opponent's disc
    // private ArrayList<Tile> availableMoves;
    private ArrayList<Pair<Integer, Integer>> availableMoves;
    private static final int BOARD_SIZE = 8;


    /**
     * Constructs and sets up an Othello Game with an 8 x 8 board for
     * the players to play with
     */
    public OthelloGameState()
    {
        // each player will start off with 2 points meaning that they each will
        // have 2 discs already on the board
        _blackScore = _whiteScore = 2;
        gameOver = false;
        blackTurn = true;
        try
        {
            gameBoard = new OthelloBoard(BOARD_SIZE);
        }
        catch (OthelloException ignored) {}
        availableMoves = findMoves();
    }


    /**
     * Returns the number of points the player with black tokens has
     */
    public int blackScore()
    {
        return _blackScore;
    }


    /**
     * Returns the number of points the player with white tokens has
     */
    public int whiteScore()
    {
        return _whiteScore;
    }


    /**
     * Returns True if the game is over and false otherwise.
     */
    public boolean isGameOver()
    {
        return gameOver;
    }


    /**
     * Returns True if it is the player's with black tokens turn. Otherwise,
     * it returns false
     */
    public boolean isBlackTurn()
    {
        return blackTurn;
    }


    /**
     * Returns true if it is the player's with white tokens turn. Otherwise,
     * it returns false
     */
    public boolean isWhiteTurn()
    {
        return !blackTurn;
    }


    /**
     * Returns true if the current player can make a move at the given
     * coordinate (ROW, COL). Otherwise, it returns false if the coordinate is
     * invalid or the player cannot make a move at that position.
     */
    public boolean isValidMove(int row, int col)
    {
        return gameBoard.isValidCoordinate(row, col) && availableMoves.stream()
                .anyMatch(coordinate -> coordinate.first == row && coordinate.second == col);
    }


    /**
     * Makes a move at the given coordinate (ROW, COL) and updates the game accordingly. However, if the player makes
     * an invalid move, an OthelloException is thrown.
     */
    public void makeMove(int row, int col) throws OthelloException
    {
        if (isValidMove(row, col))
        {
            gameBoard.placeTokenAt(row, col, playerToken());
            updateScore(flankTiles(row, col));
            gameOver = updateTurn();
        }
        else
        {
            throw new OthelloException("Current player cannot make a move at coordinate(row=" + row + ", col=" + col + ").");
        }
    }


    /**
     * Returns the board of the current othello game state.
     */
    public OthelloBoard board()
    {
        return gameBoard;
    }


    /**
     * Returns an exact copy of the current othello game state (i.e. the copy will have the same scores, a copy of the
     * underlying game board, etc.)
     */
    @Override
    public Object clone()
    {
        try
        {
            OthelloGameState clonedGame = (OthelloGameState) super.clone();
            clonedGame.gameBoard = (OthelloBoard) gameBoard.clone();
            clonedGame.availableMoves = (ArrayList<Pair<Integer, Integer>>) availableMoves.clone();
            return clonedGame;
        }

        catch (CloneNotSupportedException exception)
        {
            return null;
        }
    }


    /**
     * Returns the color token of the current player's turn.
     */
    public OthelloTileState playerToken()
    {
        return (blackTurn? OthelloTileState.BLACK : OthelloTileState.WHITE);
    }

    /**
     * Returns the color token of the opponent's token (opponent means here the player who's waiting for their turn)
     */
    public OthelloTileState opponentToken()
    {
        return (playerToken() == OthelloTileState.BLACK? OthelloTileState.WHITE : OthelloTileState.BLACK);
    }

    /**
     * Updates whose turn it is and updates their list of available moves they can make. However, if the current player has
     * no moves to make in their current position, then it is automatically the other player's turn. It returns false if
     * there is at least one move is available from either player which indicates that the game is not over yet.
     * Otherwise, it returns true to indicate that the game is over since both players can no longer make anymore moves.
     *
     */
    private boolean updateTurn()
    {
        blackTurn = !blackTurn;
        availableMoves = findMoves();

        // current player has no moves to make; thus, it's the other player's turn
        if (availableMoves.isEmpty())
        {
            blackTurn = !blackTurn;
            availableMoves = findMoves();
            // if both have no moves to make, then the game is over
            return availableMoves.isEmpty();
        }

        return false;
    }


    /**
     * Finds the available moves/tiles that the current player can place his token on
     */
    private ArrayList<Pair<Integer, Integer>> findMoves()
    {
        ArrayList<Pair<Integer, Integer>> moves = new ArrayList<>();
        for (Tile tile : gameBoard)
        {
            if (tile.state() == OthelloTileState.EMPTY && canPlaceTokenAt(tile))
            {
                moves.add(new Pair<>(tile.row(), tile.col()));
            }
        }
        return moves;
    }


    /**
     * Returns true if the current player can place his token at the given empty tile. Otherwise it returns false
     */
    private boolean canPlaceTokenAt(Tile tile)
    {
        // checks in all eight possible directions to find at least one opponent's token can be outflanked
        return     canPlaceTokenAt(tile.row(), tile.col() + 1, 0, 1)
                || canPlaceTokenAt(tile.row(), tile.col() - 1, 0, -1)
                || canPlaceTokenAt(tile.row() + 1, tile.col(), 1, 0)
                || canPlaceTokenAt(tile.row() - 1, tile.col(), -1, 0)
                || canPlaceTokenAt(tile.row() + 1, tile.col() + 1, 1, 1)
                || canPlaceTokenAt(tile.row() - 1, tile.col() + 1, -1, 1)
                || canPlaceTokenAt(tile.row() + 1, tile.col() - 1, 1, -1)
                || canPlaceTokenAt(tile.row() - 1, tile.col() - 1, -1, -1);
    }


    /**
     * Returns if the current player can place his token at the given coordinate (ROW, COL) heading
     * towards a given direction determined by the given deltas and false otherwise.
     */
    private boolean canPlaceTokenAt(int row, int col, int deltax, int deltay)
    {
        return !findFlankableTiles(row, col, deltax, deltay).isEmpty();
    }


    /**
     * Finds all the flankable tiles (i.e. tiles that can be flipped to their opposite color)
     * that the player made with his recent move.
     */
    private ArrayList<Pair<Integer, Integer>> findFlankableTiles(int row, int col)
    {
        ArrayList<Pair<Integer, Integer>> flankableTiles = new ArrayList<>();
        flankableTiles.addAll(findFlankableTiles(row, col + 1, 0, 1));
        flankableTiles.addAll(findFlankableTiles(row, col - 1, 0, -1));
        flankableTiles.addAll(findFlankableTiles(row + 1, col, 1, 0));
        flankableTiles.addAll(findFlankableTiles(row - 1, col, -1, 0));
        flankableTiles.addAll(findFlankableTiles(row - 1, col + 1, -1, 1));
        flankableTiles.addAll(findFlankableTiles(row + 1, col - 1, 1, -1));
        flankableTiles.addAll(findFlankableTiles(row - 1, col - 1, -1, -1));
        flankableTiles.addAll(findFlankableTiles(row + 1, col + 1, 1, 1));
        return flankableTiles;
    }


    /**
     * Returns a list of all the tile coordinates that can be outflanked by the player's move, starting from (ROW, COL) and heading
     * towards a given direction determined by the given deltas.
     */
    private ArrayList<Pair<Integer, Integer>> findFlankableTiles(int row, int col, int deltax, int deltay)
    {
        boolean flankable = false;
        ArrayList<Pair<Integer, Integer>> flankableTiles = new ArrayList<>();
        OthelloTileState opponentToken = opponentToken();

        while (gameBoard.isValidCoordinate(row, col))
        {
            OthelloTileState state = gameBoard.tileStateAt(row, col);
            // turns true if there is at least one's opponent token in the direction it is checking for
            flankable = (!flankable? state == opponentToken : flankable);

            // cannot flank opponent's token if it couldn't find an opponent's token in the direction it is checking
            // or couldn't find the current player's token in the direction in a row filled with at least one opponent's disc
            // at the end
            if (state == OthelloTileState.EMPTY || state == playerToken() && !flankable)
            {
                break;
            }
            else if (state == playerToken() && flankable)
            {
                // can outflank if it found the current's player token at the end of the opponent's row of his own token(s)
                return flankableTiles;
            }
            flankableTiles.add(new Pair<>(row, col));
            row += deltax;
            col += deltay;
        }

        return new ArrayList<>();
    }


    /**
     * Flanks all the opponent tiles beginning at (ROW, COL) all the way until it finds the current player's token at
     * the opposite end. Then, it returns the number of tiles that were flanked with this move.
     */
    private int flankTiles(int row, int col) throws OthelloException
    {
        int tilesFlanked = 0;
        for (Pair<Integer, Integer> coordinate : findFlankableTiles(row, col))
        {
            gameBoard.flankTile(coordinate.first, coordinate.second);
            ++tilesFlanked;
        }
        return tilesFlanked;
    }


    /**
     * Updates the scores of both players. The player who made the last move increases his score by the number of tiles
     * they have flanked plus the additional tile they have placed to make their move. Then it decreases the other
     * player's score by the number of tiles that were flanked since they lost those tiles.
     */
    private void updateScore(int tilesFlanked)
    {
        // the total score should be total tiles flanked and the token they placed at (ROW, COL)
        if (blackTurn)
        {
            _blackScore += tilesFlanked + 1;
            _whiteScore -= tilesFlanked;
        }
        else
        {
            _whiteScore += tilesFlanked + 1;
            _blackScore -= tilesFlanked;
        }
    }
}
