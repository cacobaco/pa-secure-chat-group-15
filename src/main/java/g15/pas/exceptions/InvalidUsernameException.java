package g15.pas.exceptions;

/**
 * This class represents a custom exception that is thrown when an invalid username error occurs.
 * It extends the Exception class and includes a static code and message for the invalid username error.
 */
public class InvalidUsernameException extends Exception {

    public static final String CODE = "INVALID_USERNAME_ERROR";
    private static final String MESSAGE = "O nome de utilizador é inválido.";

    /**
     * Default constructor for InvalidUsernameException.
     * It initializes the superclass with the default message.
     */
    public InvalidUsernameException() {
        super(MESSAGE);
    }

    /**
     * Constructor for InvalidUsernameException with a cause.
     * It initializes the superclass with the message and cause from the Throwable.
     *
     * @param cause The Throwable that caused the exception.
     */
    public InvalidUsernameException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructor for InvalidUsernameException with a custom message.
     * It initializes the superclass with the custom message.
     *
     * @param message The custom message for the exception.
     */
    public InvalidUsernameException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidUsernameException with a custom message and a cause.
     * It initializes the superclass with the custom message and the cause from the Throwable.
     *
     * @param message The custom message for the exception.
     * @param cause   The Throwable that caused the exception.
     */
    public InvalidUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

}