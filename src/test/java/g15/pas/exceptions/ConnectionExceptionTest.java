package g15.pas.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ConnectionExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        ConnectionException exception = new ConnectionException();
        assertEquals("Ocorreu um erro ao estabelecer uma conex\u00E3o.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new RuntimeException("Test exception");
        ConnectionException exception = new ConnectionException(cause);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        ConnectionException exception = new ConnectionException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        Throwable cause = new RuntimeException("Test exception");
        ConnectionException exception = new ConnectionException("Custom message", cause);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
