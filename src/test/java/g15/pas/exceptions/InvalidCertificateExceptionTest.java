package g15.pas.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InvalidCertificateExceptionTest {

    @Test
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidCertificateException exception = new InvalidCertificateException();
        assertEquals("O certificado \u00E9 inv\u00E1lido.", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidCertificateException exception = new InvalidCertificateException(cause);
        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldCreateExceptionWithCustomMessage() {
        InvalidCertificateException exception = new InvalidCertificateException("Custom message");
        assertEquals("Custom message", exception.getMessage());
    }

    @Test
    void shouldCreateExceptionWithCustomMessageAndCause() {
        Throwable cause = new RuntimeException("Test exception");
        InvalidCertificateException exception = new InvalidCertificateException("Custom message", cause);
        assertEquals("Custom message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

}
