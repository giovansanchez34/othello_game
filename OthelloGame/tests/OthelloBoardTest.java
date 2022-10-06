
import gamelogic.OthelloBoard;
import gamelogic.OthelloTileState;
import gamelogic.Tile;
import gamelogic.OthelloException;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Tests the behavior of the board class.
 */
class OthelloBoardTest
{
    @Test
    public void canConstructABoard()
    {
        for (int i = 6; i <= 12; ++i)
        {
            int finalI = i;
            assertDoesNotThrow(() -> new OthelloBoard(finalI));
        }
    }

    @Test
    public void cannotConstructABoardLessThan4AndBiggerThan12()
    {
        for (int i = 0; i <= 20; ++i)
        {
            int finalI = i;
            assertThrows(OthelloException.class, () -> new OthelloBoard(finalI));
            if (i == 5)
            {
                i = 13;
            }
        }
    }

    @Test
    public void anEightByEightBoardHasFourTokensInMiddle()
    {
        assertDoesNotThrow(() -> {
            OthelloBoard b = new OthelloBoard(8);
            for (Tile tile : b)
            {
                if (tile.row() == 4 && tile.col() == 3)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 4 && tile.col() == 4)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
                else if (tile.row() == 3 && tile.col() == 3)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
                else if (tile.row() == 3 && tile.col() == 4)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
            }
        });
    }


    @Test
    public void aSixBySixBoardHasFourTokensInMiddle()
    {
        assertDoesNotThrow(() -> {
            OthelloBoard b = new OthelloBoard(6);
            for (Tile tile : b)
            {
                if (tile.row() == 2 && tile.col() == 2)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
                else if (tile.row() == 2 && tile.col() == 3)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 3 && tile.col() == 2)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 3 && tile.col() == 3)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
            }
        });
    }

    @Test
    public void aTwelveByTwelveBoardHasFourTokensInMiddle()
    {
        assertDoesNotThrow(() -> {
            OthelloBoard b = new OthelloBoard(12);
            for (Tile tile : b)
            {
                if (tile.row() == 6 && tile.col() == 6)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
                else if (tile.row() == 6 && tile.col() == 5)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 5 && tile.col() == 6)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 5 && tile.col() == 5)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
            }
        });
    }

    @Test
    public void aElevenByElevenBoardHasFourTokensInMiddle()
    {
        assertDoesNotThrow(() -> {
            OthelloBoard b = new OthelloBoard(11);
            for (Tile tile : b)
            {
                if (tile.row() == 5 && tile.col() == 5)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
                else if (tile.row() == 4 && tile.col() == 5)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 5 && tile.col() == 4)
                {
                    assertEquals(OthelloTileState.BLACK, tile.state());
                }
                else if (tile.row() == 4 && tile.col() == 4)
                {
                    assertEquals(OthelloTileState.WHITE, tile.state());
                }
            }
        });
    }

    @Test
    public void canGetRowsAndCols()
    {
        try
        {
            OthelloBoard b1 = new OthelloBoard(8);
            OthelloBoard b2 = new OthelloBoard(6);
            OthelloBoard b3 = new OthelloBoard(12);
            assertEquals(8, b1.rows());
            assertEquals(6, b2.rows());
            assertEquals(12, b3.rows());
            assertEquals(8, b1.cols());
            assertEquals(6, b2.cols());
            assertEquals(12, b3.cols());
        }

        catch (OthelloException exception)
        {
            fail("Should not throw an Othello Exception");
        }
    }

    @Test
    public void validCoordinatesAreValid()
    {
        try
        {
            OthelloBoard b1 = new OthelloBoard(8);
            OthelloBoard b2 = new OthelloBoard(6);
            OthelloBoard b3 = new OthelloBoard(12);

            assertTrue(b1.isValidCoordinate(0, 0));
            assertTrue(b1.isValidCoordinate(0, 7));
            assertTrue(b1.isValidCoordinate(7, 0));
            assertTrue(b1.isValidCoordinate(7, 7));
            assertTrue(b1.isValidCoordinate(4, 3));
            assertTrue(b1.isValidCoordinate(5, 3));
            assertTrue(b1.isValidCoordinate(6, 7));
            assertTrue(b1.isValidCoordinate(4, 2));

            assertTrue(b2.isValidCoordinate(0, 0));
            assertTrue(b2.isValidCoordinate(0, 5));
            assertTrue(b2.isValidCoordinate(5, 0));
            assertTrue(b2.isValidCoordinate(5, 5));
            assertTrue(b2.isValidCoordinate(1, 4));
            assertTrue(b2.isValidCoordinate(3, 5));
            assertTrue(b2.isValidCoordinate(2, 3));
            assertTrue(b2.isValidCoordinate(2, 5));

            assertTrue(b3.isValidCoordinate(0, 0));
            assertTrue(b3.isValidCoordinate(0, 11));
            assertTrue(b3.isValidCoordinate(11, 0));
            assertTrue(b3.isValidCoordinate(11, 11));
            assertTrue(b3.isValidCoordinate(0, 7));
            assertTrue(b3.isValidCoordinate(7, 11));
            assertTrue(b3.isValidCoordinate(3, 6));
            assertTrue(b3.isValidCoordinate(8, 6));

        }

        catch (OthelloException exception)
        {
            fail("Should not throw an Othello Exception");
        }
    }

    @Test
    public void invalidCoordinatesAreInvalid()
    {
        try
        {
            OthelloBoard b1 = new OthelloBoard(8);

            assertFalse(b1.isValidCoordinate(-1, 0));
            assertFalse(b1.isValidCoordinate(8, 7));
            assertFalse(b1.isValidCoordinate(0, -1));
            assertFalse(b1.isValidCoordinate(7, 8));

        }

        catch (OthelloException exception) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void canGetStateOfTile()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            assertEquals(OthelloTileState.EMPTY, b.tileStateAt(0, 0));
            assertEquals(OthelloTileState.EMPTY, b.tileStateAt(0, 7));
            assertEquals(OthelloTileState.EMPTY, b.tileStateAt(7, 7));
            assertEquals(OthelloTileState.EMPTY, b.tileStateAt(7, 0));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void canSetTileState()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            b.setTileAt(0, 0, OthelloTileState.BLACK);
            assertEquals(OthelloTileState.BLACK, b.tileStateAt(0, 0));
            b.setTileAt(0, 7, OthelloTileState.WHITE);
            assertEquals(OthelloTileState.WHITE, b.tileStateAt(0, 7));
            b.setTileAt(7, 7, OthelloTileState.EMPTY);
            assertEquals(OthelloTileState.EMPTY, b.tileStateAt(7, 7));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void canPlaceTokensAtEmptyTiles()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            assertDoesNotThrow(() -> b.placeTokenAt(0, 0, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(1, 0, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(0, 1, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(0, 7, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(7, 7, OthelloTileState.WHITE));
        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void cannotPlaceTokensAtNonEmptyTiles()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            assertDoesNotThrow(() -> b.placeTokenAt(0, 0, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(1, 0, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(0, 1, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(0, 7, OthelloTileState.WHITE));
            assertDoesNotThrow(() -> b.placeTokenAt(7, 7, OthelloTileState.WHITE));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(0, 0, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(1,0, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(0, 1, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(0, 7, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(7, 7, OthelloTileState.BLACK));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void cannotPlaceTokensAtNonExistentTiles()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            assertThrows(OthelloException.class, () -> b.placeTokenAt(-1, 0, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(1,-1, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(-5, 1, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(10, 7, OthelloTileState.BLACK));
            assertThrows(OthelloException.class, () -> b.placeTokenAt(8, 7, OthelloTileState.BLACK));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void canFlankNonEmptyTiles()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);
            b.placeTokenAt(0, 0, OthelloTileState.BLACK);
            b.placeTokenAt(5, 0, OthelloTileState.WHITE);
            b.placeTokenAt(0, 5, OthelloTileState.WHITE);
            b.placeTokenAt(2, 4, OthelloTileState.WHITE);
            b.placeTokenAt(4, 7, OthelloTileState.BLACK);

            b.flankTile(0, 0);
            b.flankTile(5, 0);
            b.flankTile(0, 5);
            b.flankTile(2, 4);
            b.flankTile(4, 7);
            b.flankTile(4, 4);

            assertEquals(OthelloTileState.WHITE, b.tileStateAt(0, 0));
            assertEquals(OthelloTileState.BLACK, b.tileStateAt(5, 0));
            assertEquals(OthelloTileState.BLACK, b.tileStateAt(0, 5));
            assertEquals(OthelloTileState.BLACK, b.tileStateAt(2, 4));
            assertEquals(OthelloTileState.WHITE, b.tileStateAt(4, 7));
            assertEquals(OthelloTileState.BLACK, b.tileStateAt(4, 4));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }




    @Test
    public void cannotFlankNonExistentTiles()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(8);

            assertThrows(OthelloException.class, () -> b.flankTile(-1, 0));
            assertThrows(OthelloException.class, () -> b.flankTile(5, -1));
            assertThrows(OthelloException.class, () -> b.flankTile(8, 5));
            assertThrows(OthelloException.class, () -> b.flankTile(2, 8));
            assertThrows(OthelloException.class, () -> b.flankTile(4, 10));
            assertThrows(OthelloException.class, () -> b.flankTile(11, 11));

        }

        catch (OthelloException exc) { fail("Should not throw an Othello Exception"); }
    }

    @Test
    public void boardCanBeRepresentedAsAString()
    {
        try
        {
            String board8x8 = "- | - | - | - | - | - | - | -\n- | - | - | - | - | - | - | -\n- | - | - | - | - | - | - | -\n"
                    + "- | - | - | W | B | - | - | -\n- | - | - | B | W | - | - | -\n- | - | - | - | - | - | - | -\n"
                    + "- | - | - | - | - | - | - | -\n- | - | - | - | - | - | - | -\n\n";
            assertEquals(board8x8, new OthelloBoard(8).toString());
        }

        catch (OthelloException ignored) {}
    }

    @Test
    public void clonedBoardShouldBeTheSameAsOriginalBoard()
    {
        try
        {
            OthelloBoard b1 = new OthelloBoard(8);
            b1.placeTokenAt(0, 0, OthelloTileState.BLACK);
            b1.placeTokenAt(0, 5, OthelloTileState.BLACK);
            b1.placeTokenAt(7, 7, OthelloTileState.BLACK);
            b1.placeTokenAt(4, 0, OthelloTileState.WHITE);
            b1.placeTokenAt(4, 5, OthelloTileState.WHITE);
            b1.flankTile(4, 4);

            OthelloBoard b2 = (OthelloBoard) b1.clone();
            assertEquals(b1.toString(), b2.toString());

        }

        catch (OthelloException ignored) {}

    }

    @Test
    public void clonedBoardShouldNotAffectOriginalBoard()
    {
        try
        {
            OthelloBoard b1 = new OthelloBoard(8);
            b1.placeTokenAt(0, 0, OthelloTileState.BLACK);
            b1.placeTokenAt(0, 5, OthelloTileState.BLACK);
            b1.placeTokenAt(7, 7, OthelloTileState.BLACK);
            b1.placeTokenAt(4, 0, OthelloTileState.WHITE);
            b1.placeTokenAt(4, 5, OthelloTileState.WHITE);
            b1.flankTile(4, 4);

            OthelloBoard b2 = (OthelloBoard) b1.clone();
            assertEquals(b1.toString(), b2.toString());
            b1.flankTile(4, 4);
            b2.placeTokenAt(7, 0, OthelloTileState.WHITE);
            b2.placeTokenAt(0, 7, OthelloTileState.WHITE);

            assertNotEquals(b1.tileStateAt(7, 0), b2.tileStateAt(7, 0));
            assertNotEquals(b1.toString(), b2.toString());
        }

        catch (OthelloException ignored) {}

    }

    @Test
    public void boardIteratorStopsAtLastCoordinate()
    {
        try
        {
            OthelloBoard b = new OthelloBoard(9);
            Iterator<Tile> i = b.iterator();
            for (int j = 0; j < 81; ++j)
            {
                i.next();
            }
            assertFalse(i.hasNext());
            assertThrows(NoSuchElementException.class, i::next);

        }

        catch (OthelloException ignored) {}
    }

//    @Test
//    public void cannotGetStateOfNonExistentTile()
//    {
//        try
//        {
//            OthelloBoard b = new OthelloBoard(8);
//            assertThrows(OthelloException.class, () -> b.tileStateAt(-1, 0));
//        }
//
//        catch (OthelloException exc) {}
//    }


}
