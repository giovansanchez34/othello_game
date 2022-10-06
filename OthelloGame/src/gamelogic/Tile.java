package gamelogic;

/**
 * Tile represents the image of a tile on
 * a Othello Game Board.
 */
public class Tile implements Cloneable
{
    // the position of the tile on a othello game board (ROW, COL)
    private final int _row;
    private final int _col;
    // the state of the tile during a othello game; can change during the game
    private OthelloTileState _state;

    /**
     * Creates a new tile at (ROW, COL) with an initial state
     */
    public Tile(int row, int col, OthelloTileState state)
    {
        this._row = row;
        this._col = col;
        this._state = state;
    }

    /**
     * Returns the row position of the tile.
     */
    public int row()
    {
        return _row;
    }

    /**
     * Returns the column position of the tile.
     */
    public int col()
    {
        return _col;
    }

    /**
     * Returns the state of the tile.
     */
    public OthelloTileState state()
    {
        return _state;
    }

    /**
     * Tile changes from its original state to the new
     * given state during a othello game.
     */
    public void setState(OthelloTileState newState)
    {
        _state = newState;
    }

    /**
     * Returns a string representation of a tile
     */
    @Override
    public String toString()
    {
        return switch (_state)
                {
                    case BLACK -> "B";
                    case WHITE -> "W";
                    default -> "-";
                };
    }


    /**
     * Creates an exact copy of the Tile with the same row, column and state.
     */
    @Override
    public Object clone()
    {
        try
        {
            return (Tile) super.clone();
        }

        catch (CloneNotSupportedException exception)
        {
            return null;
        }
    }

    /**
     * Flanks a tile from its opposite color. A white tile turns into a black tile and a black tile turns into a white
     * tile. If the tile is empty, nothing happens.
     */
    public void flankTile()
    {
        switch (_state)
        {
            case WHITE -> setState(OthelloTileState.BLACK);
            case BLACK -> setState(OthelloTileState.WHITE);
        }
    }


}