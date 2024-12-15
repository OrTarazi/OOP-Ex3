package exceptions;

public class InvalidCharsetSizeException extends Exception {
    private static final String INVALID_CHARSET_SIZE_MESSAGE = "Did not execute. Charset is too small.";

    public InvalidCharsetSizeException() {
        super(INVALID_CHARSET_SIZE_MESSAGE);
    }
}
