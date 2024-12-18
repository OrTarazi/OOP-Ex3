package exceptions;

/**
 * Exception thrown when the provided character set size is invalid.
 * This exception indicates that the requested operation cannot be performed because the character set does
 * not meet the minimum required size.
 * The exception message provides a specific error message to convey the reason for the failure.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidCharsetSizeException extends Exception {
    private static final String INVALID_CHARSET_SIZE_MESSAGE = "Did not execute. Charset is too small.";

    /**
     * Constructs a new `InvalidCharsetSizeException` with a default error message.
     */
    public InvalidCharsetSizeException() {
        super(INVALID_CHARSET_SIZE_MESSAGE);
    }
}
