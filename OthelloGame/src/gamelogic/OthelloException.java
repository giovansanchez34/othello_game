package gamelogic;

/**
 * An exception that is thrown when something wrong occurs during the run of an OthelloGame
 */
public class OthelloException extends Exception
{
    /**
     * Constructs an othello exception and the reason why it is being thrown
     */
    public OthelloException(String reason)
    {
        super(reason);
    }
}
