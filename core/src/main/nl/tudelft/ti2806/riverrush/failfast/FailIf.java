package nl.tudelft.ti2806.riverrush.failfast;

/**
 * Utility class for developing code that fails fast.
 */
public final class FailIf {

    /**
     * Cannot construct, utility class.
     */
    private FailIf() { }

    /**
     * Throws {@link AssertionError} when one of the arguments is null.
     * @param queries - All objects to check for null.
     */
    public static void isNull(final Object ... queries) {
        for (Object q : queries) {
            if (q == null) {
                throw new NullException("Parameter should not be null!");
            }
        }
    }
}
