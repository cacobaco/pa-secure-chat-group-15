package g15.pas.exceptions;

public class InvalidUsernameException extends Exception {

    public static final String CODE = "INVALID_USERNAME_ERROR";
    private static final String MESSAGE = "O nome de utilizador é inválido.";

    public InvalidUsernameException() {
        super(MESSAGE);
    }

    public InvalidUsernameException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidUsernameException(String message) {
        super(message);
    }

    public InvalidUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

}
