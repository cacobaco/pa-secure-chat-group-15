package g15.pas.exceptions;

public class InvalidInfoException extends Exception {

    public static final String CODE = "INVALID_INFO_EXCEPTION";
    private static final String MESSAGE = "A info é inválida.";

    public InvalidInfoException() {
        super(MESSAGE);
    }

    public InvalidInfoException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidInfoException(String message) {
        super(message);
    }

    public InvalidInfoException(String message, Throwable cause) {
        super(message, cause);
    }

}
