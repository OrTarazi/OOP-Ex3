package exceptions;

/**
 * Exception thrown when an invalid resolution format is specified for the ASCII art generation.
 *
 * <p>This exception indicates that the requested resolution format is incorrect and the resolution
 * could not be changed.</p>
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidResolutionFormatException extends Exception {
    private static final String INVALID_RESOLUTION_FORMAT_MESSAGE =
            "Did not change resolution due to incorrect format.";

    /**
     * Constructs a new `InvalidResolutionFormatException` with a default error message.
     */
    public InvalidResolutionFormatException() {
        super(INVALID_RESOLUTION_FORMAT_MESSAGE);
    }
}
