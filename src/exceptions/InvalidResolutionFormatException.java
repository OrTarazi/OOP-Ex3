package exceptions;

/**
//todo doc:
 */
public class InvalidResolutionFormatException extends Exception {
    private static final String INVALID_RESOLUTION_FORMAT_MESSAGE = "Did not change resolution due to " +
            "incorrect format.";

    /**
     * Constructs a new `InvalidAddFormatException` with a default error message.
     */
    public InvalidResolutionFormatException() {
        super(INVALID_RESOLUTION_FORMAT_MESSAGE);
    }
}
