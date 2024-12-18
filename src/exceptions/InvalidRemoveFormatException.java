package exceptions;

/**
 * Exception thrown when the format of a "remove" operation is invalid.
 * This exception indicates that the input provided for removing an item does not conform to the
 * expected format.
 * The exception message provides a specific error message to describe the reason for the failure.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidRemoveFormatException extends Exception {
    private static final String INVALID_REMOVE_FORMAT = "Did not remove due to incorrect format.";

    /**
     * Constructs a new `InvalidRemoveFormatException` with a default error message.
     */
    public InvalidRemoveFormatException() {
        super(INVALID_REMOVE_FORMAT);
    }
}

