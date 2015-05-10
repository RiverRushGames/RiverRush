package nl.tudelft.ti2806.riverrush.network.protocol;

/**
 * Exception thrown when an invalid actions is supplied.
 */
public class InvalidActionException extends RuntimeException {
    /**
     * Construct the exception with a usefull message.
     * @param message - Some very informative message.
     */
    public InvalidActionException(final String message) {
        super(message);
    }
}
