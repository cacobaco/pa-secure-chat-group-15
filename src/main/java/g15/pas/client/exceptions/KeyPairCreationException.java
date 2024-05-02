package g15.pas.client.exceptions;

public class KeyPairCreationException extends Exception {

    public static final String CODE = "KEY_PAIR_CREATION_ERROR";
    private static final String DEFAULT_MESSAGE = "An error occurred while creating the key pair.";

    public KeyPairCreationException() {
        super(DEFAULT_MESSAGE);
    }

    public KeyPairCreationException(Throwable cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public KeyPairCreationException(String message) {
        super(message);
    }

    public KeyPairCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
