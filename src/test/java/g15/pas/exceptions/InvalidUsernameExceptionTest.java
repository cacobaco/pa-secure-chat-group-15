package g15.pas.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidUsernameExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidUsernameException exception = new InvalidUsernameException();
        assertEquals("O nome de utilizador \u00E9 inv\u00E1lido.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidUsernameException exception = new InvalidUsernameException(cause);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        InvalidUsernameException exception = new InvalidUsernameException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidUsernameException exception = new InvalidUsernameException("Custom message", cause);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
