package g15.pas.utils;

import org.junit.jupiter.api.Test;
import java.security.MessageDigest;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrityTest {

    @Test
    public void testGenerateDigest() {
        try {
            byte[] message = "Hello, Integrity!".getBytes();

            byte[] digest = Integrity.generateDigest(message);

            assertNotNull(digest, "O digest não deve ser nulo");
            assertTrue(digest.length > 0, "O digest deve ter comprimento maior que zero");
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testVerifyDigest_Valid() {
        try {
            byte[] message = "Hello, Integrity!".getBytes();

            byte[] digest = MessageDigest.getInstance("SHA-512").digest(message);

            boolean result = Integrity.verifyDigest(digest, digest);

            assertTrue(result, "O digest deve ser considerado válido");
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testVerifyDigest_Invalid() {
        try {
            byte[] message = "Hello, Integrity!".getBytes();

            byte[] digest1 = MessageDigest.getInstance("SHA-512").digest(message);
            byte[] digest2 = MessageDigest.getInstance("SHA-512").digest("Hello, OpenAI!".getBytes());

            boolean result = Integrity.verifyDigest(digest1, digest2);

            assertFalse(result, "O digest deve ser considerado inválido");
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

}

