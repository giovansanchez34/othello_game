package othello_players;

/**
 * A Factory that generates any type of othello players: a human player, an AI, etc.
 */
public class OthelloPlayerFactory
{
    /**
     * Creates and returns a BasicAI instance - this AI uses the minimax algorithm to look three moves ahead
     * and decide which move is the best one to make at its current state
     */
    public static OthelloPlayer createOthelloBasicAI()
    {
        return new BasicAI();
    }

    /**
     * Creates and returns an OthelloConsolePlayer instance - this is only used for console version of the game that
     * allows the user to enter their moves.
     */
    public static OthelloPlayer createOthelloConsolePlayer()
    {
        return new OthelloConsolePlayer();
    }

    /**
     * Creates and returns an OthelloHumanPlayer instance - this is only used for GUI version of the game that allows
     * the user to enter their move.
     */
    public static OthelloPlayer createOthelloHumanPlayer()
    {
        return new HumanPlayer();
    }

    /**
     * Creates and returns a RandomAI instance - this AI chooses any available move based on its random behavior.
     */
    public static OthelloPlayer createRandomAI()
    {
        return new RandomAI();
    }

    /**
     * Creates and returns a StaticEvaluatorAI instance - this AI is more advanced than the basic AI in terms of speed
     * and knowledge of the game.
     */
    public static OthelloPlayer createStaticEvaluatorAI()
    {
        return new StaticEvaluatorAI();
    }

    /**
     * Creates and returns a DepthChangerAI instance - this AI is similar to the static evaluator, but it will change
     * the depth of its search depending on the state of the game.
     */
    public static OthelloPlayer createDepthChangerAI()
    {
        return new DepthChangerAI();
    }

    /**
     * Creates and returns a DynamicEvaluatorAI instance - this AI is more advanced than the Static AI since it uses
     * different evaluation functions at different stages of the game.
     */
    public static OthelloPlayer createDynamicEvaluatorAI()
    {
        return new DynamicEvaluatorAI();
    }


    /**
     * Creates and returns an OthelloPlayer based on the given playerType code. If the given code does not exist, it just
     * returns an OthelloHumanPlayer.
     */
    public static OthelloPlayer createAPlayer(String playerType)
    {
        playerType = playerType.toUpperCase();
        if (playerType.equals("BASIC AI"))
        {
            return OthelloPlayerFactory.createOthelloBasicAI();
        }
        else if (playerType.equals("CONSOLE"))
        {
            return OthelloPlayerFactory.createOthelloConsolePlayer();
        }
        else if (playerType.equals("RANDOM AI"))
        {
            return OthelloPlayerFactory.createRandomAI();
        }
        else if (playerType.equals("STATIC AI"))
        {
            return OthelloPlayerFactory.createStaticEvaluatorAI();
        }
        else if (playerType.equals("DYNAMIC AI"))
        {
            return OthelloPlayerFactory.createDynamicEvaluatorAI();
        }
        else if (playerType.equals("DEPTH CHANGER AI"))
        {
            return OthelloPlayerFactory.createDepthChangerAI();
        }
        else
        {
            // If playerType.equals("HUMAN") or anything else, it just returns a HumanPlayer
            return OthelloPlayerFactory.createOthelloHumanPlayer();
        }
    }


    private OthelloPlayerFactory(){}
}
