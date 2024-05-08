package g15.pas.exceptions;

/**
 * This class represents a custom exception that is thrown when a key pair creation error occurs.
 * It extends the Exception class and includes a static code and message for the key pair creation error.
 */
public class KeyPairCreationException extends Exception {

    public static final String CODE = "KEY_PAIR_CREATION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao gerar um par de chaves.";

    /**
     * Default constructor for KeyPairCreationException.
     * It initializes the superclass with the default message.
     */
    public KeyPairCreationException() {
        super(MESSAGE);
    }

    /**
     * Constructor for KeyPairCreationException with a cause.
     * It initializes the superclass with the default message and cause from the Throwable.
     *
     * @param cause The Throwable that caused the exception.
     */
    public KeyPairCreationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * Constructor for KeyPairCreationException with a custom message.
     * It initializes the superclass with the custom message.
     *
     * @param message The custom message for the exception.
     */
    public KeyPairCreationException(String message) {
        super(message);
    }

    /**
     * Constructor for KeyPairCreationException with a custom message and a cause.
     * It initializes the superclass with the custom message and the cause from the Throwable.
     *
     * @param message The custom message for the exception.
     * @param cause   The Throwable that caused the exception.
     */
    public KeyPairCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
