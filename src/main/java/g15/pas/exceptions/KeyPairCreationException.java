package g15.pas.exceptions;

public class KeyPairCreationException extends Exception {

    public static final String CODE = "KEY_PAIR_CREATION_ERROR";
    private static final String MESSAGE = "Ocorreu um erro ao gerar um par de chaves.";

    public KeyPairCreationException() {
        super(MESSAGE);
    }

    public KeyPairCreationException(Throwable cause) {
        super(MESSAGE, cause);
    }

    public KeyPairCreationException(String message) {
        super(message);
    }

    public KeyPairCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
