
import gamelogic.OthelloGameState;
import gamelogic.OthelloException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


/**
 * Tests the behavior of the othello game state class
 */
class OthelloGameStateTest
{
    OthelloGameState game;


    @BeforeEach
    public void setUp()
    {
        game = new OthelloGameState();
    }


    @Test
    public void canConstructOthelloGame()
    {
        assertEquals(2, game.blackScore());
        assertEquals(2, game.whiteScore());
        assertFalse(game.isGameOver());
        assertTrue(game.isBlackTurn());
        assertFalse(game.isWhiteTurn());
    }

    @Test
    public void blackTokenPlayerCanMakeAMove()
    {

        assertDoesNotThrow(() -> game.makeMove(2, 3));
        assertEquals(4, game.blackScore());
        assertEquals(1, game.whiteScore());
        assertFalse(game.isBlackTurn());
        assertTrue(game.isWhiteTurn());
    }


    @Test
    public void whiteTokenPlayerCanMakeAMove()
    {
        assertDoesNotThrow(() -> game.makeMove(2, 3));
        assertDoesNotThrow(() -> game.makeMove(2, 4));
        assertEquals(3, game.blackScore());
        assertEquals(3, game.whiteScore());
        assertFalse(game.isGameOver());
        assertTrue(game.isBlackTurn());
    }

    @Test
    public void clonedOthelloGameIsNotTheSameAsOriginal()
    {
        OthelloGameState g = (OthelloGameState) game.clone();
        assertNotSame(g, game);
    }

    @Test
    public void clonedOthelloGameShouldHaveDifferentBoardObjects()
    {
        OthelloGameState g = (OthelloGameState) game.clone();
        assertNotSame(g.board(), game.board());
        assertDoesNotThrow(()->g.makeMove(2, 3));
        assertNotEquals(g.board().toString(), game.board().toString());
        assertEquals(4, g.blackScore());
        assertEquals(2, game.blackScore());

        assertEquals(1, g.whiteScore());
        assertEquals(2, game.whiteScore());

    }

    @Test
    public void clonedOthelloGameShouldNotAffectOriginalCopy()
    {
        OthelloGameState g = (OthelloGameState) game.clone();
        assertDoesNotThrow(()->g.makeMove(2, 3));
        assertDoesNotThrow(()->g.makeMove(2, 4));
        assertDoesNotThrow(()->g.makeMove(2, 5));
        assertDoesNotThrow(()->g.makeMove(4, 2));
        assertEquals(3, g.whiteScore());
        assertEquals(5, g.blackScore());

        assertEquals(2, game.whiteScore());
        assertEquals(2, game.blackScore());
        assertNotEquals(game.board().toString(), g.board().toString());
    }

    @Test
    public void invalidMovesAreInvalid()
    {
        for (int row = 0; row < game.board().rows(); ++row)
        {
            for (int col = 0; col < game.board().cols(); ++col)
            {
                if ((row != 2 || col != 3) && (row != 5 || col != 4) && (row != 3 || col != 2) && (row != 4 || col != 5))
                {
                    assertFalse(game.isValidMove(row, col));
                }
            }
        }
    }

    @Test
    public void validMovesAreValid()
    {
        for (int row = 0; row < game.board().rows(); ++row)
        {
            for (int col = 0; col < game.board().cols(); ++col)
            {
                if ((row == 2 && col == 3) || (row == 5 && col == 4) || (row == 3 && col == 2) || (row == 4 && col == 5))
                {
                    assertTrue(game.isValidMove(row, col));
                }
            }
        }
    }


    @Test
    public void cannotMakeInvalidMoves()
    {
        for (int row = 0; row < game.board().rows(); ++row)
        {
            for (int col = 0; col < game.board().cols(); ++col)
            {
                if ((row != 2 || col != 3) && (row != 5 || col != 4) && (row != 3 || col != 2) && (row != 4 || col != 5))
                {
                    int finalRow = row;
                    int finalCol = col;
                    assertThrows(OthelloException.class, ()->game.makeMove(finalRow, finalCol));
                }
            }
        }
    }

    @Test
    public void canDetectAvailableMovesThatAreOnTheRightAndLeft()
    {
        assertTrue(game.isValidMove(4, 5));
        assertTrue(game.isValidMove(3, 2));
    }

    @Test
    public void canDetectAvailableMovesThatAreAboveAndBottom()
    {
        assertTrue(game.isValidMove(2, 3));
        assertTrue(game.isValidMove(5, 4));
    }

    @Test
    public void canDetectAvailableMovesThatAreOnTheDiagonals()
    {
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertTrue(game.isValidMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(5, 3));

        assertTrue(game.isValidMove(6, 6));
        assertTrue(game.isValidMove(6, 3));
        assertTrue(game.isValidMove(6, 2));
        assertTrue(game.isValidMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertTrue(game.isValidMove(7, 7));
        assertTrue(game.isValidMove(3, 5));
    }

    @Test
    public void newMovesAreValidAfterAMoveIsMade()
    {
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertTrue(game.isValidMove(2, 4));
        assertTrue(game.isValidMove(4, 2));
        assertTrue(game.isValidMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(4, 2));

        assertTrue(game.isValidMove(5, 1));
        assertTrue(game.isValidMove(5, 2));
        assertTrue(game.isValidMove(5, 3));
        assertTrue(game.isValidMove(5, 4));
        assertTrue(game.isValidMove(5, 5));

        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertTrue(game.isValidMove(1, 3));
        assertTrue(game.isValidMove(2, 4));
        assertTrue(game.isValidMove(4, 5));
        assertTrue(game.isValidMove(2, 5));

        assertDoesNotThrow(()->game.makeMove(1, 3));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(4, 0));
        assertDoesNotThrow(()->game.makeMove(7, 7));

        assertTrue(game.isValidMove(2, 4));
        assertTrue(game.isValidMove(4, 5));
        assertTrue(game.isValidMove(2, 5));

        assertEquals(4, game.whiteScore());
        assertEquals(9, game.blackScore());
    }

    @Test
    public void noMoreMovesAreAvailableWhenGameIsOver()
    {
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(1, 3));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(4, 0));
        assertDoesNotThrow(()->game.makeMove(7, 7));
        assertDoesNotThrow(()->game.makeMove(2, 4));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(3, 5));
        assertDoesNotThrow(()->game.makeMove(1, 4));
        assertDoesNotThrow(()->game.makeMove(2, 5));
        assertDoesNotThrow(()->game.makeMove(0, 4));
        assertDoesNotThrow(()->game.makeMove(1, 5));
        assertDoesNotThrow(()->game.makeMove(0, 6));
        assertDoesNotThrow(()->game.makeMove(6, 4));
        assertDoesNotThrow(()->game.makeMove(7, 4));
        assertEquals(14, game.whiteScore());
        assertEquals(10, game.blackScore());
        assertDoesNotThrow(()->game.makeMove(0, 5));
        assertDoesNotThrow(()->game.makeMove(0, 2));
        assertDoesNotThrow(()->game.makeMove(5, 1));
        assertDoesNotThrow(()->game.makeMove(6, 0));
        assertDoesNotThrow(()->game.makeMove(3, 1));
        assertDoesNotThrow(()->game.makeMove(2, 0));
        assertDoesNotThrow(()->game.makeMove(1, 6));
        assertDoesNotThrow(()->game.makeMove(0, 7));
        assertDoesNotThrow(()->game.makeMove(1, 7));
        assertDoesNotThrow(()->game.makeMove(2, 7));
        assertDoesNotThrow(()->game.makeMove(1, 2));
        assertDoesNotThrow(()->game.makeMove(1, 1));
        assertDoesNotThrow(()->game.makeMove(0, 1));
        assertDoesNotThrow(()->game.makeMove(0, 0));
        assertDoesNotThrow(()->game.makeMove(1, 0));
        assertDoesNotThrow(()->game.makeMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(7, 3));
        assertDoesNotThrow(()->game.makeMove(2, 6));
        assertDoesNotThrow(()->game.makeMove(7, 5));
        assertDoesNotThrow(()->game.makeMove(5,6));
        assertDoesNotThrow(()->game.makeMove(4, 6));
        assertDoesNotThrow(()->game.makeMove(5, 7));
        assertDoesNotThrow(()->game.makeMove(3, 6));
        assertDoesNotThrow(()->game.makeMove(3, 7));
        assertDoesNotThrow(()->game.makeMove(5, 2));
        assertDoesNotThrow(()->game.makeMove(7, 6));
        assertDoesNotThrow(()->game.makeMove(3, 0));
        assertDoesNotThrow(()->game.makeMove(7, 2));
        assertDoesNotThrow(()->game.makeMove(7, 1));
        assertDoesNotThrow(()->game.makeMove(6, 1));
        assertDoesNotThrow(()->game.makeMove(5, 0));
        assertDoesNotThrow(()->game.makeMove(7, 0));
        assertDoesNotThrow(()->game.makeMove(2, 1));
        assertDoesNotThrow(()->game.makeMove(6, 2));
        assertDoesNotThrow(()->game.makeMove(3, 2));
        assertDoesNotThrow(()->game.makeMove(6, 3));
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(6, 5));
        assertDoesNotThrow(()->game.makeMove(4, 7));
        assertDoesNotThrow(()->game.makeMove(6, 7));

        assertEquals(39, game.whiteScore());
        assertEquals(25, game.blackScore());
        assertTrue(game.isGameOver());

        for (int row = 0; row < game.board().rows(); ++row)
        {
            for (int col = 0; col < game.board().cols(); ++col)
            {
                assertFalse(game.isValidMove(row, col));
            }
        }
    }



    @Test
    public void gameCanEndEarlyWhenAPlayerHasNoMoreTokensOnBoard()
    {
        OthelloGameState clone = (OthelloGameState) game.clone();
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(3, 5));
        assertDoesNotThrow(()->game.makeMove(2, 4));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(4, 6));
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(6, 4));

        assertEquals(13, game.blackScore());
        assertEquals(0, game.whiteScore());
        assertTrue(game.isGameOver());

        for (int row = 0; row < game.board().rows(); ++row)
        {
            for (int col = 0; col < game.board().cols(); ++col)
            {
                assertFalse(game.isValidMove(row, col));
            }
        }

        assertDoesNotThrow(()->clone.makeMove(5, 4));
        assertDoesNotThrow(()->clone.makeMove(3, 5));
        assertDoesNotThrow(()->clone.makeMove(2, 4));
        assertDoesNotThrow(()->clone.makeMove(1, 3));
        assertDoesNotThrow(()->clone.makeMove(2, 6));
        assertDoesNotThrow(()->clone.makeMove(6, 4));
        assertDoesNotThrow(()->clone.makeMove(3, 2));
        assertDoesNotThrow(()->clone.makeMove(2, 1));
        assertDoesNotThrow(()->clone.makeMove(1, 4));
        assertDoesNotThrow(()->clone.makeMove(0, 4));
        assertDoesNotThrow(()->clone.makeMove(5, 3));
        assertDoesNotThrow(()->clone.makeMove(5, 2));
        assertDoesNotThrow(()->clone.makeMove(4, 2));
        assertDoesNotThrow(()->clone.makeMove(1, 7));
        assertDoesNotThrow(()->clone.makeMove(6, 2));
        assertDoesNotThrow(()->clone.makeMove(7, 2));
        assertDoesNotThrow(()->clone.makeMove(6, 3));
        assertDoesNotThrow(()->clone.makeMove(2, 2));
        assertDoesNotThrow(()->clone.makeMove(3, 1));
        assertDoesNotThrow(()->clone.makeMove(2, 0));
        assertDoesNotThrow(()->clone.makeMove(2, 5));
        assertDoesNotThrow(()->clone.makeMove(1, 6));
        assertDoesNotThrow(()->clone.makeMove(2, 3));
        assertDoesNotThrow(()->clone.makeMove(7, 3));
        assertTrue(game.isGameOver());
    }

    @Test
    public void gameCanSkipTurnIfPlayerHasNoMoreMoves()
    {
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(1, 3));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(5, 1));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(4, 6));
        assertDoesNotThrow(()->game.makeMove(3, 1));
        assertTrue(game.isValidMove(2, 0));
        assertTrue(game.isValidMove(4, 0));
        assertTrue(game.isValidMove(6, 0));
        assertDoesNotThrow(()->game.makeMove(6, 0));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(4, 0));
        assertDoesNotThrow(()->game.makeMove(3, 5));
        assertTrue(game.isValidMove(2, 0));
        assertTrue(game.isValidMove(2, 1));
        assertTrue(game.isValidMove(2, 2));
        assertTrue(game.isValidMove(2, 4));
        assertTrue(game.isValidMove(2, 5));
        assertTrue(game.isValidMove(3, 6));
        assertTrue(game.isValidMove(5, 2));
        assertTrue(game.isValidMove(5, 5));
        assertTrue(game.isValidMove(6, 3));
        assertTrue(game.isValidMove(6, 4));
        assertDoesNotThrow(()->game.makeMove(2, 4));
        assertDoesNotThrow(()->game.makeMove(1, 4));
        assertDoesNotThrow(()->game.makeMove(0, 4));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(6, 4));
        assertDoesNotThrow(()->game.makeMove(6, 1));
        assertDoesNotThrow(()->game.makeMove(6, 2));
        assertDoesNotThrow(()->game.makeMove(7, 1));
        assertDoesNotThrow(()->game.makeMove(2, 0));

        assertDoesNotThrow(()->game.makeMove(6, 3));
        assertDoesNotThrow(()->game.makeMove(7, 3));
        assertDoesNotThrow(()->game.makeMove(2, 1));
        assertDoesNotThrow(()->game.makeMove(1, 1));
        assertDoesNotThrow(()->game.makeMove(6, 5));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(0, 1));
        assertEquals(8, game.whiteScore());
        assertEquals(26, game.blackScore());
        assertDoesNotThrow(()->game.makeMove(0, 2));
        assertDoesNotThrow(()->game.makeMove(0, 5));
        assertDoesNotThrow(()->game.makeMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(1, 5));
        assertDoesNotThrow(()->game.makeMove(0, 6));
        assertTrue(game.isWhiteTurn());
        assertDoesNotThrow(()->game.makeMove(0, 7));
        assertTrue(game.isWhiteTurn());
        assertFalse(game.isBlackTurn());
        assertDoesNotThrow(()->game.makeMove(7, 4));
        assertDoesNotThrow(()->game.makeMove(7, 5));
        assertDoesNotThrow(()->game.makeMove(0, 0));
        assertDoesNotThrow(()->game.makeMove(3, 2));
        assertDoesNotThrow(()->game.makeMove(7, 2));
        assertDoesNotThrow(()->game.makeMove(2, 5));
        assertDoesNotThrow(()->game.makeMove(2, 6));
        assertDoesNotThrow(()->game.makeMove(1, 2));

    }


    @Test
    public void gameCanEndEarlyWithBoardPartiallyFull()
    {
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(5, 6));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(7, 6));
        assertDoesNotThrow(()->game.makeMove(7, 7));
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertDoesNotThrow(()->game.makeMove(7, 5));
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(3, 2));

        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(1, 1));
        assertDoesNotThrow(()->game.makeMove(0, 0));
        assertDoesNotThrow(()->game.makeMove(6, 5));
        assertDoesNotThrow(()->game.makeMove(3, 5));

        assertDoesNotThrow(()->game.makeMove(2, 1));
        assertDoesNotThrow(()->game.makeMove(1, 0));

        assertDoesNotThrow(()->game.makeMove(7, 4));
        assertDoesNotThrow(()->game.makeMove(7, 3));
        assertDoesNotThrow(()->game.makeMove(6, 7));
        assertDoesNotThrow(()->game.makeMove(5, 7));
        assertDoesNotThrow(()->game.makeMove(4, 7));
        assertDoesNotThrow(()->game.makeMove(3, 7));
        assertDoesNotThrow(()->game.makeMove(2, 5));
        assertDoesNotThrow(()->game.makeMove(1, 5));
        assertDoesNotThrow(()->game.makeMove(2, 0));
        assertDoesNotThrow(()->game.makeMove(3, 0));
        assertDoesNotThrow(()->game.makeMove(0, 1));
        assertDoesNotThrow(()->game.makeMove(1, 2));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(5, 2));
        assertDoesNotThrow(()->game.makeMove(3, 1));
        assertDoesNotThrow(()->game.makeMove(0, 2));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(4, 0));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(5, 1));
        assertDoesNotThrow(()->game.makeMove(6, 2));
        assertTrue(game.isWhiteTurn());
        assertDoesNotThrow(()->game.makeMove(7, 2));
        assertFalse(game.isBlackTurn());
        assertTrue(game.isWhiteTurn());
        assertDoesNotThrow(()->game.makeMove(0, 4));
        assertTrue(game.isGameOver());
        assertEquals(45, game.whiteScore());
        assertEquals(0, game.blackScore());
    }


    @Test
    public void gameCanEndEarlyEvenIfBothPlayersHaveTokensOnBoard()
    {

        assertDoesNotThrow(()->game.makeMove(3, 2));
        assertDoesNotThrow(()->game.makeMove(2, 4));
        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(5, 2));
        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(5, 6));
        assertDoesNotThrow(()->game.makeMove(6, 4));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(6, 3));
        assertDoesNotThrow(()->game.makeMove(5, 1));
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertDoesNotThrow(()->game.makeMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(3, 5));
        assertDoesNotThrow(()->game.makeMove(7, 5));
        assertDoesNotThrow(()->game.makeMove(6, 2));
        assertDoesNotThrow(()->game.makeMove(7, 2));
        assertDoesNotThrow(()->game.makeMove(2, 5));
        assertDoesNotThrow(()->game.makeMove(1, 5));
        assertDoesNotThrow(()->game.makeMove(6, 5));
        assertDoesNotThrow(()->game.makeMove(7, 4));
        assertDoesNotThrow(()->game.makeMove(2, 1));
        assertDoesNotThrow(()->game.makeMove(1, 0));
        assertDoesNotThrow(()->game.makeMove(7, 3));
        assertDoesNotThrow(()->game.makeMove(1, 4));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(1, 3));
        assertDoesNotThrow(()->game.makeMove(0, 4));
        assertDoesNotThrow(()->game.makeMove(0, 5));
        assertDoesNotThrow(()->game.makeMove(1, 2));
        assertDoesNotThrow(()->game.makeMove(0, 2));
        assertDoesNotThrow(()->game.makeMove(0, 6));
        assertDoesNotThrow(()->game.makeMove(1, 6));
        assertDoesNotThrow(()->game.makeMove(0, 7));
        assertDoesNotThrow(()->game.makeMove(1, 7));
        assertDoesNotThrow(()->game.makeMove(2, 7));
        assertDoesNotThrow(()->game.makeMove(2, 6));
        assertDoesNotThrow(()->game.makeMove(0, 1));
        assertDoesNotThrow(()->game.makeMove(1, 1));
        assertDoesNotThrow(()->game.makeMove(0, 0));
        assertDoesNotThrow(()->game.makeMove(3, 1));
        assertDoesNotThrow(()->game.makeMove(3, 7));
        assertDoesNotThrow(()->game.makeMove(4, 6));
        assertDoesNotThrow(()->game.makeMove(3, 6));
        assertDoesNotThrow(()->game.makeMove(4, 7));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(5, 7));
        assertDoesNotThrow(()->game.makeMove(6, 7));
        assertDoesNotThrow(()->game.makeMove(3, 0));
        assertDoesNotThrow(()->game.makeMove(2, 0));
        assertDoesNotThrow(()->game.makeMove(7, 7));
        assertDoesNotThrow(()->game.makeMove(4, 0));
        assertDoesNotThrow(()->game.makeMove(7, 6));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(5, 0));
        assertDoesNotThrow(()->game.makeMove(7, 1));
        assertTrue(game.isGameOver());
        assertEquals(57, game.blackScore());
        assertEquals(4, game.whiteScore());
    }

    @Test
    public void gameCanEndEarlyEvenIfNoMoreMovesAreAvailable()
    {

        assertDoesNotThrow(()->game.makeMove(4, 5));
        assertDoesNotThrow(()->game.makeMove(5, 3));
        assertDoesNotThrow(()->game.makeMove(3, 2));
        assertDoesNotThrow(()->game.makeMove(4, 6));
        assertDoesNotThrow(()->game.makeMove(6, 3));
        assertDoesNotThrow(()->game.makeMove(2, 4));
        assertDoesNotThrow(()->game.makeMove(5, 5));
        assertDoesNotThrow(()->game.makeMove(5, 4));
        assertDoesNotThrow(()->game.makeMove(3, 5));
        assertDoesNotThrow(()->game.makeMove(5, 6));
        assertDoesNotThrow(()->game.makeMove(1, 5));
        assertDoesNotThrow(()->game.makeMove(3, 6));
        assertDoesNotThrow(()->game.makeMove(3, 7));
        assertDoesNotThrow(()->game.makeMove(4, 2));
        assertDoesNotThrow(()->game.makeMove(5, 2));
        assertDoesNotThrow(()->game.makeMove(2, 7));
        assertDoesNotThrow(()->game.makeMove(6, 4));


        assertDoesNotThrow(()->game.makeMove(4, 7));
        assertDoesNotThrow(()->game.makeMove(5, 7));
        assertDoesNotThrow(()->game.makeMove(6, 7));
        assertDoesNotThrow(()->game.makeMove(6, 6));
        assertDoesNotThrow(()->game.makeMove(2, 5));
        assertDoesNotThrow(()->game.makeMove(2, 6));
        assertDoesNotThrow(()->game.makeMove(3, 1));
        assertDoesNotThrow(()->game.makeMove(2, 3));
        assertDoesNotThrow(()->game.makeMove(4, 1));
        assertDoesNotThrow(()->game.makeMove(2, 2));
        assertDoesNotThrow(()->game.makeMove(1, 7));
        assertDoesNotThrow(()->game.makeMove(5, 0));
        assertDoesNotThrow(()->game.makeMove(7, 7));
        assertDoesNotThrow(()->game.makeMove(2, 1));
        assertDoesNotThrow(()->game.makeMove(6, 5));
        assertDoesNotThrow(()->game.makeMove(7, 5));
        assertDoesNotThrow(()->game.makeMove(7, 6));
        assertDoesNotThrow(()->game.makeMove(0, 5));

        assertDoesNotThrow(()->game.makeMove(1, 6));
        assertDoesNotThrow(()->game.makeMove(0, 7));
        assertDoesNotThrow(()->game.makeMove(0, 6));
        assertDoesNotThrow(()->game.makeMove(0, 4));
        assertDoesNotThrow(()->game.makeMove(0, 3));
        assertDoesNotThrow(()->game.makeMove(0, 2));
        assertDoesNotThrow(()->game.makeMove(1, 2));
        assertDoesNotThrow(()->game.makeMove(1, 4));
        assertDoesNotThrow(()->game.makeMove(1, 3));
        assertDoesNotThrow(()->game.makeMove(7, 4));
        assertDoesNotThrow(()->game.makeMove(7, 3));
        assertDoesNotThrow(()->game.makeMove(6, 2));
        assertDoesNotThrow(()->game.makeMove(3, 0));
        assertTrue(game.isGameOver());
        assertEquals(1, game.blackScore());
        assertEquals(51, game.whiteScore());
    }
}