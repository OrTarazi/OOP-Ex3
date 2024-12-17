package exceptions;

public class InvalidOutputFormatException extends Exception {
    private static final String INVALID_OUTPUT_METHOD_FORMAT_MESSAGE = "Did not change output method due to " +
            "incorrect format.";

    public InvalidOutputFormatException() {super(INVALID_OUTPUT_METHOD_FORMAT_MESSAGE);}
}
