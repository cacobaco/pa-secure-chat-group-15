package g15.pas.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IntegrityTest {

    @Test
    void testGenerateMAC() {
        try {
            byte[] message = "Hello, MAC!".getBytes();
            byte[] macKey = "SecretKey".getBytes();

            byte[] mac = Integrity.generateMAC(message, macKey);

            assertNotNull(mac);
            assertTrue(mac.length > 0);
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    void testVerifyMAC_Valid() {
        try {
            byte[] message = "Hello, MAC!".getBytes();
            byte[] macKey = "SecretKey".getBytes();
            byte[] mac = Integrity.generateMAC(message, macKey);

            boolean result = Integrity.verifyMAC(mac, mac);

            assertTrue(result, "O MAC deve ser considerado válido");
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    void testVerifyMAC_Invalid() {
        try {
            byte[] message = "Hello, MAC!".getBytes();
            byte[] macKey = "SecretKey".getBytes();
            byte[] mac = Integrity.generateMAC(message, macKey);
            byte[] differentMac = "DifferentMAC".getBytes();

            boolean result = Integrity.verifyMAC(mac, differentMac);

            assertFalse(result, "O MAC deve ser considerado inválido");
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }
}

