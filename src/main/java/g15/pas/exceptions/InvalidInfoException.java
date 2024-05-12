package g15.pas.exceptions;

/**
 * This class represents an invalid information exception that extends the Exception class.
 * It provides several constructors to create an InvalidInfoException with a message, cause, or both.
 * It also contains a static final code and message for an invalid information error.
 */
public class InvalidInfoException extends Exception {

    public static final String CODE = "INVALID_INFO_EXCEPTION";
    private static final String MESSAGE = "A info é inválida.";

    /**
     * Constructs a new InvalidInfoException with the default invalid information error message.
     */
    public InvalidInfoException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new InvalidInfoException with the specified cause.
     * The message is set to the message of the cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidInfoException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructs a new InvalidInfoException with the specified message.
     *
     * @param message the message of the exception
     */
    public InvalidInfoException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidInfoException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public InvalidInfoException(String message, Throwable cause) {
        super(message, cause);
    }

}
