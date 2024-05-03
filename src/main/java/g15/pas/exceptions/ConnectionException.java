package g15.pas.exceptions;

public class ConnectionException extends Exception {

    public static final String CODE = "CONNECTION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao estabelecer uma conexão.";

    public ConnectionException() {
        super(MESSAGE);
    }

    public ConnectionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public ConnectionException(String message) {
        super(message);
    }

    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

}
