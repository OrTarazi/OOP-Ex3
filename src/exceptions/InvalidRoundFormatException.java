package exceptions;

/**
 * Exception thrown when an invalid rounding format is encountered.
 * This exception indicates that the specified rounding format is incorrect.
 *
 * @author Or Tarazi, Agam Hershko
 */
public class InvalidRoundFormatException extends Exception {

    private static final String INVALID_ROUND_MESSAGE =
            "Did not change rounding method due to incorrect format.";

    /**
     * Constructs a new `InvalidRoundFormatException` with a default error message.
     */
    public InvalidRoundFormatException() {
        super(INVALID_ROUND_MESSAGE);
    }
}