package g15.pas.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InvalidInfoExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidInfoException exception = new InvalidInfoException();

        assertNotNull(exception);
        assertEquals("A info é inválida.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        String errorMessage = "Custom error message";
        InvalidInfoException exception = new InvalidInfoException(errorMessage);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new IllegalArgumentException("Error cause");
        InvalidInfoException exception = new InvalidInfoException(cause);

        assertNotNull(exception);
        assertEquals("Error cause", exception.getCause().getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        String errorMessage = "Custom error message";
        Throwable cause = new IllegalArgumentException("Error cause");
        InvalidInfoException exception = new InvalidInfoException(errorMessage, cause);

        assertNotNull(exception);
        assertEquals(errorMessage, exception.getMessage());
        assertEquals("Error cause", exception.getCause().getMessage());
    }
}