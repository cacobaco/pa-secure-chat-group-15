package g15.pas.exceptions;

import g15.pas.exceptions.InvalidCommandException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidCommandExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidCommandException exception = new InvalidCommandException();
        assertEquals("O comando \u00E9 inv\u00E1lido.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidCommandException exception = new InvalidCommandException(cause);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        InvalidCommandException exception = new InvalidCommandException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidCommandException exception = new InvalidCommandException("Custom message", cause);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
