package exceptions;

/**
 * Exception thrown when the format of an "add" operation is invalid.
 * This exception indicates that the input provided for adding an item does not conform to the
 * expected format.
 * The exception message is set to a predefined error message indicating the issue.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidAddFormatException extends Exception {
    private static final String INVALID_ADD_FORMAT = "Did not add due to incorrect format.";

    /**
     * Constructs a new `InvalidAddFormatException` with a default error message.
     */
    public InvalidAddFormatException() {
        super(INVALID_ADD_FORMAT);
    }
}
