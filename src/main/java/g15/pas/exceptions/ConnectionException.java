package g15.pas.exceptions;

/**
 * This class represents a connection exception that extends the Exception class.
 * It provides several constructors to create a ConnectionException with a message, cause, or both.
 * It also contains a static final code and message for a connection error.
 */
public class ConnectionException extends Exception {

    public static final String CODE = "CONNECTION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao estabelecer uma conexão.";

    /**
     * Constructs a new ConnectionException with the default connection error message.
     */
    public ConnectionException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new ConnectionException with the specified cause.
     * The message is set to the message of the cause.
     *
     * @param cause the cause of the exception
     */
    public ConnectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructs a new ConnectionException with the specified message.
     *
     * @param message the message of the exception
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * Constructs a new ConnectionException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
