package exceptions;

// TODO: Add documentation
public class InvalidRoundFormatException extends Exception {

    private static final String INVALID_ROUND_MESSAGE =
            "Did not change rounding method due to incorrect format.";

    public InvalidRoundFormatException() {
        super(INVALID_ROUND_MESSAGE);
    }
}
