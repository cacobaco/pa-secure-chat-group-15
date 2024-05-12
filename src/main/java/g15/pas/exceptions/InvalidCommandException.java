package g15.pas.exceptions;

/**
 * This class represents an invalid command exception that extends the Exception class.
 * It provides several constructors to create an InvalidCommandException with a message, cause, or both.
 * It also contains a static final code and message for an invalid command error.
 */
public class InvalidCommandException extends Exception {

    public static final String CODE = "INVALID_COMMAND_EXCEPTION";
    private static final String MESSAGE = "O comando é inválido.";

    /**
     * Constructs a new InvalidCommandException with the default invalid command error message.
     */
    public InvalidCommandException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new InvalidCommandException with the specified cause.
     * The message is set to the message of the cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidCommandException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructs a new InvalidCommandException with the specified message.
     *
     * @param message the message of the exception
     */
    public InvalidCommandException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidCommandException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
