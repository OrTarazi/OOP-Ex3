package exceptions;

public class InvalidResolutionValueException extends Exception {

    private static final String INVALID_RESOLUTION_VALUE_MESSAGE = "Did not change resolution due to exceeding " +
            "boundaries.";

    public InvalidResolutionValueException() {super(INVALID_RESOLUTION_VALUE_MESSAGE);}
}
