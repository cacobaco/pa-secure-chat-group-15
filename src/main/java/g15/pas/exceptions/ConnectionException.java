package g15.pas.exceptions;

/**
 * This class represents a custom exception that is thrown when a connection error occurs.
 * It extends the Exception class and includes a static code and message for the connection error.
 */
public class ConnectionException extends Exception {

    public static final String CODE = "CONNECTION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao estabelecer uma conexão.";

    /**
     * Default constructor for ConnectionException.
     * It initializes the superclass with the default message.
     */
    public ConnectionException() {
        super(MESSAGE);
    }

    /**
     * Constructor for ConnectionException with a cause.
     * It initializes the superclass with the message and cause from the Throwable.
     *
     * @param cause The Throwable that caused the exception.
     */
    public ConnectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructor for ConnectionException with a custom message.
     * It initializes the superclass with the custom message.
     *
     * @param message The custom message for the exception.
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * Constructor for ConnectionException with a custom message and a cause.
     * It initializes the superclass with the custom message and the cause from the Throwable.
     *
     * @param message The custom message for the exception.
     * @param cause   The Throwable that caused the exception.
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
