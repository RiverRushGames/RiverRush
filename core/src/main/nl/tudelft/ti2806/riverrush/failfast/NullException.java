package nl.tudelft.ti2806.riverrush.failfast;

/**
 * Thrown when some assertion yields a null pointer.
 * Different from {@link NullPointerException} as this exception only occurs in checked places.
 */
public final class NullException extends FailFastException {

    /**
     * Construct the exception with a clarifying message.
     * @param message - The message.
     */
    public NullException(final String message) {
        super(message);
    }
}
