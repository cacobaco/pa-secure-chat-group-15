package g15.pas.exceptions;

/**
 * This class represents a custom exception that is thrown when an invalid command error occurs.
 * It extends the Exception class and includes a static code and message for the invalid command error.
 */
public class InvalidCommandException extends Exception {

    public static final String CODE = "INVALID_COMMAND_EXCEPTION";
    private static final String MESSAGE = "O comando é inválido.";

    /**
     * Default constructor for InvalidCommandException.
     * It initializes the superclass with the default message.
     */
    public InvalidCommandException() {
        super(MESSAGE);
    }

    /**
     * Constructor for InvalidCommandException with a cause.
     * It initializes the superclass with the message and cause from the Throwable.
     *
     * @param cause The Throwable that caused the exception.
     */
    public InvalidCommandException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructor for InvalidCommandException with a custom message.
     * It initializes the superclass with the custom message.
     *
     * @param message The custom message for the exception.
     */
    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidCommandException with a custom message and a cause.
     * It initializes the superclass with the custom message and the cause from the Throwable.
     *
     * @param message The custom message for the exception.
     * @param cause   The Throwable that caused the exception.
     */
    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
