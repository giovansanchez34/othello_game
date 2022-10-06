package gamelogic;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.Stream;


/**
 * OthelloBoard represents a NxN othello game board and it portrays the state of
 * an othello game.
 */
public class OthelloBoard implements Iterable<Tile>, Cloneable
{
    // describes an N x N board game
    private final int size;
    // holds the contents of the game board
    private Tile[][] board;
    // a board cannot have less than 4 rows and 4 columns or cannot have more than 12 rows and 12 columns
    private static final int MINIMUM_BOARD_SIZE = 6;
    private static final int MAXIMUM_BOARD_SIZE = 12;


    /**
     * Constructs and set ups an N x N board for the players to play with. If the given size for the board is less
     * than 4, then an exception is thrown since it is too small
     */
    public OthelloBoard(int size) throws OthelloException
    {
        if (size > MAXIMUM_BOARD_SIZE || size < MINIMUM_BOARD_SIZE)
        {
            throw new OthelloException("Size(" + size + ") of Othello Board is too big. It must be less than 9 and bigger than 3.");
        }
        this.size = size;
        setUpBoard();
    }


    /**
     * Returns the number of rows the game board contains.
     */
    public int rows()
    {
        return size;
    }


    /**
     * Returns the number of columns the game board contains.
     */
    public int cols()
    {
        return size;
    }


    /**
     * Returns an iterator that can iterate through all tiles in entire underlying game board.
     */
    @Override
    public Iterator<Tile> iterator()
    {
        return new BoardIterator();
    }


    /**
     * Returns true if the given coordinate (ROW, COL) is a valid position in the othello game board.
     */
    public boolean isValidCoordinate(int row, int col)
    {
        return 0 <= row && row < size && 0 <= col && col < size;
    }


    /**
     * Returns the state of the tile at the given coordinate (ROW, COL). The state of the tile will either
     * be empty, black or white
     */
    public OthelloTileState tileStateAt(int row, int col)
    {
        return board[row][col].state();
    }


    /**
     * Sets the state (black, white or empty) of the tile at the given coordinate (ROW, COL).
     */
    public void setTileAt(int row, int col, OthelloTileState state)
    {
        board[row][col].setState(state);
    }


    /**
     * Changes an empty tile at the given coordinate (ROW, COL) to either be black or white. If the given coordinate is
     * invalid or the tile is not empty, an OthelloException is thrown
     */
    public void placeTokenAt(int row, int col, OthelloTileState state) throws OthelloException
    {
        if (!isValidCoordinate(row, col))
        {
            throw new OthelloException("Coordinate(row=" + row + ", col=" + col + ") is an invalid position.");
        }

        Tile tile = board[row][col];
        if (tile.state() == OthelloTileState.EMPTY)
        {
            tile.setState(state);
        }
        else
        {
            throw new OthelloException("Coordinate(row=" + row + ",col=" + col + ") contains a non-empty tile");
        }
    }


    /**
     * Flanks a tile at the given coordinate (ROW, COL). It flips the tile from to its opposite color (Black turns
     * to white and white turns to Black). If the tile is empty, nothing happens
     */
    public void flankTile(int row, int col) throws OthelloException
    {
        if (!isValidCoordinate(row, col))
        {
            throw new OthelloException("Coordinate(row=" + row + ",col=" + col + ") is an empty tile. Cannot flank an empty tile");
        }
        board[row][col].flankTile();
    }


    /**
     * Returns a neatly-formatted othello game board that represents the current state of an othello game.
     */
    @Override
    public String toString()
    {
        StringBuilder gameBoard = new StringBuilder();
        for (Tile tile : this)
        {
            // separates the rows from each other
            if (tile.col() == size - 1)
            {
                gameBoard.append(tile + "\n");
            }

            else
            {
                // "|" separates the game pieces in the cell from each other
                gameBoard.append(tile + " | ");
            }
        }
        return gameBoard.append("\n").toString();
    }


    /**
     * Returns a copy of the current OthelloBoard with the same game state (the size of the board is the same,
     * the tiles' contents are the same, etc.)
     */
    @Override
    public Object clone()
    {
        try
        {
            OthelloBoard clonedBoard = (OthelloBoard) super.clone();

            // clones all the tiles in the underlying othello game board so that the two objects
            // don't share references
            clonedBoard.board = Stream.of(clonedBoard.board)
                    .map((tiles) -> Stream.of(tiles)
                            .map(tile -> (Tile) tile.clone())
                            .toArray(Tile[]::new))
                    .toArray(Tile[][]::new);

            return clonedBoard;
        }

        catch (CloneNotSupportedException exception)
        {
            return null;
        }
    }


    /**
     * Sets up the board by creating empty tiles and placing the first pieces of game:
     * 2 black and 2 white tokens are approximately placed in the middle of the board.
     */
    private void setUpBoard()
    {
        board = new Tile[size][size];
        for (int row = 0; row < size; ++row)
        {
            for (int col = 0; col < size; ++col)
            {
                board[row][col] = new Tile(row, col, OthelloTileState.EMPTY);
            }
        }
        placeTokens();
    }


    /**
     * Every game will start off with four already placed tokens on the board. If the size of the board is even
     * (i.e size % 2 == 0), then the tokens will be right in the middle of the board. Otherwise, the tokens will be
     * approximately placed close to the middle.
     */
    private void placeTokens()
    {
        int setUpRow = size / 2;
        int setUpCol = size / 2;
        // 2 tokens from each opponent are placed onto the board
        setTileAt(setUpRow, setUpCol, OthelloTileState.WHITE);
        setTileAt(setUpRow - 1, setUpCol, OthelloTileState.BLACK);
        setTileAt(setUpRow, setUpCol - 1, OthelloTileState.BLACK);
        setTileAt(setUpRow - 1, setUpCol - 1, OthelloTileState.WHITE);
    }



    /**
     * BoardIterator is an iterator that iterates through all the tiles in the othello board
     */
    private class BoardIterator implements Iterator<Tile>
    {
        // keeps track of the current position of the BoardIterator in the othello board
        private int row;
        private int col;

        /**
         * Constructs a BoardIterator that always begins at coordinate (0, 0) of the othello board
         */
        public BoardIterator()
        {
            row = col = 0;
        }

        /**
         * Returns true if the iterator can still traverse the othello board. Otherwise, it returns false
         * if it has reached the end of the board.
         */
        @Override
        public boolean hasNext()
        {
            // the last position of the othello board is (size - 1, size - 1). Thus if row > size
            // then there are no more tiles to iterate through.
            return row != size;
        }

        /**
         * The iterator moves onto the next valid position in the board. Then it returns the tile it was previously referring
         * to. If the iterator has reached the end of the board, it throws a NoSuchElementException.
         */
        @Override
        public Tile next()
        {
            if (row >= size)
            {
                throw new NoSuchElementException("");
            }
            Tile tile = board[row][col];
            row = (col + 1 == size? row + 1 : row);
            col = (col + 1) % size;
            return tile;
        }

    }
}