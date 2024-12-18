package exceptions;

/**
 * Exception thrown when an invalid output format is specified for the ASCII art generation.
 *
 * <p>This exception indicates that the requested output method format does not match any of the
 * supported formats.</p>
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidOutputFormatException extends Exception {
    private static final String INVALID_OUTPUT_METHOD_FORMAT_MESSAGE =
            "Did not change output method due to incorrect format.";

    /**
     * Constructs a new `InvalidOutputFormatException` with a default error message.
     */
    public InvalidOutputFormatException() {
        super(INVALID_OUTPUT_METHOD_FORMAT_MESSAGE);
    }
}
