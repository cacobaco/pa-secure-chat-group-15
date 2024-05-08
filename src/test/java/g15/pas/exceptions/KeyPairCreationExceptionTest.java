package g15.pas.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class KeyPairCreationExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        KeyPairCreationException exception = new KeyPairCreationException();
        assertEquals("Ocorreu um erro ao gerar um par de chaves.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new RuntimeException("Test exception");
        KeyPairCreationException exception = new KeyPairCreationException(cause);
        assertEquals("Ocorreu um erro ao gerar um par de chaves.", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        KeyPairCreationException exception = new KeyPairCreationException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        Throwable cause = new RuntimeException("Test exception");
        KeyPairCreationException exception = new KeyPairCreationException("Custom message", cause);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
