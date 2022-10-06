import gamelogic.Tile;
import gamelogic.OthelloTileState;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * Tests the behavior of the tile class
 */
class TileTest
{
    @Test
    public void canConstructTileOfAnyState()
    {
        Tile tile1 = new Tile(4, 4, OthelloTileState.WHITE);
        Tile tile2 = new Tile(4, 4, OthelloTileState.BLACK);
        Tile tile3 = new Tile(4, 4, OthelloTileState.EMPTY);
    }

    @Test
    public void canGetRowOfTile()
    {
        int[] rows = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Tile[] tiles = new Tile[10];
        for (int index = 0; index < tiles.length; ++index)
        {
            tiles[index] = new Tile(rows[index], 0, OthelloTileState.WHITE);
        }
        for (int index = 0; index < tiles.length; ++index)
        {
            assertEquals(rows[index], tiles[index].row());
        }
    }

    @Test
    public void canGetColOfTile()
    {
        int[] cols = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        Tile[] tiles = new Tile[10];
        for (int index = 0; index < tiles.length; ++index)
        {
            tiles[index] = new Tile(0, cols[index], OthelloTileState.WHITE);
        }
        for (int index = 0; index < tiles.length; ++index)
        {
            assertEquals(cols[index], tiles[index].col());
        }
    }

    @Test
    public void canGetStateOfTile()
    {
        Tile tile = new Tile(0, 0, OthelloTileState.WHITE);
        assertEquals(OthelloTileState.WHITE, tile.state());
    }


    @Test
    public void canSetStateOfTile()
    {
        Tile tile = new Tile(0, 0, OthelloTileState.WHITE);
        assertEquals(OthelloTileState.WHITE, tile.state());
        tile.setState(OthelloTileState.BLACK);
        assertEquals(OthelloTileState.BLACK, tile.state());
        tile.setState(OthelloTileState.EMPTY);
        assertEquals(OthelloTileState.EMPTY, tile.state());
    }

    @Test
    public void tileCanTransformIntoAppropriateString()
    {
        Tile tile = new Tile(0, 0, OthelloTileState.WHITE);
        assertEquals("W", tile.toString());
        tile.setState(OthelloTileState.BLACK);
        assertEquals("B", tile.toString());
        tile.setState(OthelloTileState.EMPTY);
        assertEquals("-", tile.toString());
    }



    @Test
    public void cloningTileProducesNewTileObject()
    {
        Tile t = new Tile(3, 6, OthelloTileState.WHITE);
        Tile clonedT = (Tile) t.clone();
        assertEquals(t.row(), clonedT.row());
        assertEquals(t.col(), clonedT.col());
        assertNotSame(t, clonedT);
    }

    @Test
    public void flankingTilesFlipsTheirColor()
    {
        Tile t = new Tile(3, 6, OthelloTileState.WHITE);
        t.flankTile();
        assertEquals(OthelloTileState.BLACK, t.state());
        t.flankTile();
        assertEquals(OthelloTileState.WHITE, t.state());
    }

    @Test
    public void flankingEmptyTileDoesNothing()
    {
        Tile t = new Tile(2, 6, OthelloTileState.EMPTY);
        t.flankTile();
        assertEquals(OthelloTileState.EMPTY, t.state());
    }


}
