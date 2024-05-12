package g15.pas.exceptions;

/**
 * This class represents an invalid certificate exception that extends the Exception class.
 * It provides several constructors to create an InvalidCertificateException with a message, cause, or both.
 * It also contains a static final code and message for an invalid certificate error.
 */
public class InvalidCertificateException extends Exception {

    public static final String CODE = "INVALID_CERTIFICATE_ERROR";
    private static final String MESSAGE = "O certificado é inválido.";

    /**
     * Constructs a new InvalidCertificateException with the default invalid certificate error message.
     */
    public InvalidCertificateException() {
        super(MESSAGE);
    }

    /**
     * Constructs a new InvalidCertificateException with the specified cause.
     * The message is set to the message of the cause.
     *
     * @param cause the cause of the exception
     */
    public InvalidCertificateException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructs a new InvalidCertificateException with the specified message.
     *
     * @param message the message of the exception
     */
    public InvalidCertificateException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidCertificateException with the specified message and cause.
     *
     * @param message the message of the exception
     * @param cause   the cause of the exception
     */
    public InvalidCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

}
