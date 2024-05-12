package g15.pas.exceptions;

/**
 * This class represents a key pair creation exception that extends the Exception class.
 * It provides several constructors to create a KeyPairCreationException with a message, cause, or both.
 * It also contains a static final code and message for a key pair creation error.
 */
public class KeyPairCreationException extends Exception {

    public static final String CODE = "KEY_PAIR_CREATION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao gerar um par de chaves.";

    /**
     * Constructs a new KeyPairCreationException with the default key pair creation error message.
     */
    public KeyPairCreationException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new KeyPairCreationException with the specified cause.
     * The message is set to the default key pair creation error message.
     *
     * @param cause the cause of the exception
     */
    public KeyPairCreationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    /**
     * Constructs a new KeyPairCreationException with the specified message.
     *
     * @param message the message of the exception
     */
    public KeyPairCreationException(String message) {
        super(message);
    }

    /**
     * Constructs a new KeyPairCreationException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public KeyPairCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
