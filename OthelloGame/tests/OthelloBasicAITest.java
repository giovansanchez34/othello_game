import othello_players.BasicAI;
import gamelogic.OthelloGameState;
import gamelogic.Pair;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


public class OthelloBasicAITest
{
    private OthelloGameState gameState;
    private BasicAI player;

    @BeforeEach
    public void setUp()
    {
        gameState = new OthelloGameState();
        player = new BasicAI();
    }

    @Test
    public void basicAIMakesValidMoves1()
    {
        Pair<Integer, Integer> move1 = player.chooseMove(gameState);
        assertTrue(gameState.isValidMove(move1.first, move1.second));
        assertDoesNotThrow(()->gameState.makeMove(move1.first, move1.second));

        Pair<Integer, Integer> move2 = player.chooseMove(gameState);
        assertTrue(gameState.isValidMove(move2.first, move2.second));
        assertDoesNotThrow(()->gameState.makeMove(move2.first, move2.second));
    }

    @Test
    public void basicAIMakesValidMovesThroughOutGame()
    {
        while (!gameState.isGameOver())
        {
            Pair<Integer, Integer> move = player.chooseMove(gameState);
            assertTrue(gameState.isValidMove(move.first, move.second));
            assertDoesNotThrow(()->gameState.makeMove(move.first, move.second));
        }
        assertEquals(46,gameState.blackScore());
        assertEquals(18,gameState.whiteScore());
    }


}
