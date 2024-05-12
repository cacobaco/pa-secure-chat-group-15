package g15.pas.exceptions;

/**
 * This class represents an invalid username exception that extends the Exception class.
 * It provides several constructors to create an InvalidUsernameException with a message, cause, or both.
 * It also contains a static final code and message for an invalid username error.
 */
public class InvalidUsernameException extends Exception {

    public static final String CODE = "INVALID_USERNAME_ERROR";
    private static final String MESSAGE = "O nome de utilizador é inválido.";

    /**
     * Constructs a new InvalidUsernameException with the default invalid username error message.
     */
    public InvalidUsernameException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new InvalidUsernameException with the specified cause.
     * The message is set to the message of the cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidUsernameException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructs a new InvalidUsernameException with the specified message.
     *
     * @param message the message of the exception
     */
    public InvalidUsernameException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidUsernameException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public InvalidUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

}
