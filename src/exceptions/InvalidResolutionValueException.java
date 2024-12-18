package exceptions;

/**
 * Exception thrown when an invalid resolution value is encountered.
 * This exception indicates that the specified resolution exceeds acceptable boundaries.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidResolutionValueException extends Exception {

    private static final String INVALID_RESOLUTION_VALUE_MESSAGE =
            "Did not change resolution due to exceeding boundaries.";

    /**
     * Constructs a new `InvalidResolutionValueException` with a default error message.
     */
    public InvalidResolutionValueException() {
        super(INVALID_RESOLUTION_VALUE_MESSAGE);
    }
}