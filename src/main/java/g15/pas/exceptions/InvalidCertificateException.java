package g15.pas.exceptions;

public class InvalidCertificateException extends Exception {

    public static final String CODE = "INVALID_CERTIFICATE_ERROR";
    private static final String MESSAGE = "O certificado é inválido.";

    public InvalidCertificateException() {
        super(MESSAGE);
    }

    public InvalidCertificateException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidCertificateException(String message) {
        super(message);
    }

    public InvalidCertificateException(String message, Throwable cause) {
        super(message, cause);
    }

}
