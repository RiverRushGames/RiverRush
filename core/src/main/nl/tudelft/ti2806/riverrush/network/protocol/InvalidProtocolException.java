package nl.tudelft.ti2806.riverrush.network.protocol;

/**
 * Exception thrown when the protocol is invalid.
 */
public class InvalidProtocolException extends RuntimeException {
    /**
     * Construct the exception with a usefull message.
     * @param message - Some very informative message.
     */
    public InvalidProtocolException(final String message) {
        super(message);
    }
}
