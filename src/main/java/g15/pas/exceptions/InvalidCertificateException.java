package g15.pas.exceptions;

/**
 * This class represents a custom exception that is thrown when an invalid certificate error occurs.
 * It extends the Exception class and includes a static code and message for the invalid certificate error.
 */
public class InvalidCertificateException extends Exception {

    public static final String CODE = "INVALID_CERTIFICATE_ERROR";
    private static final String MESSAGE = "O certificado é inválido.";

    /**
     * Default constructor for InvalidCertificateException.
     * It initializes the superclass with the default message.
     */
    public InvalidCertificateException() {
        super(MESSAGE);
    }

    /**
     * Constructor for InvalidCertificateException with a cause.
     * It initializes the superclass with the message and cause from the Throwable.
     *
     * @param cause The Throwable that caused the exception.
     */
    public InvalidCertificateException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    /**
     * Constructor for InvalidCertificateException with a custom message.
     * It initializes the superclass with the custom message.
     *
     * @param message The custom message for the exception.
     */
    public InvalidCertificateException(String message) {
        super(message);
    }

    /**
     * Constructor for InvalidCertificateException with a custom message and a cause.
     * It initializes the superclass with the custom message and the cause from the Throwable.
     *
     * @param message The custom message for the exception.
     * @param cause   The Throwable that caused the exception.
     */
    public InvalidCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

}
