package g15.pas.exceptions;

public class InvalidCommandException extends Exception {

    public static final String CODE = "INVALID_COMMAND_EXCEPTION";
    private static final String MESSAGE = "O comando é inválido.";

    public InvalidCommandException() {
        super(MESSAGE);
    }

    public InvalidCommandException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidCommandException(String message) {
        super(message);
    }

    public InvalidCommandException(String message, Throwable cause) {
        super(message, cause);
    }

}
