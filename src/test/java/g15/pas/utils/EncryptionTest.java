package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {

    @Test
    void shouldSuccessfullyEncryptAndDecryptMessageWithAES() throws Exception {
        String originalMessage = "Hello, World!";
        byte[] secretKey = new byte[16];
        new Random().nextBytes(secretKey); // generate a random AES key
        byte[] encryptedMessage = Encryption.encryptAES(originalMessage.getBytes(), secretKey);
        byte[] decryptedMessage = Encryption.decryptAES(encryptedMessage, secretKey);
        assertEquals(originalMessage, new String(decryptedMessage));
    }

    @Test
    void shouldFailDecryptionWithWrongAESKey() throws Exception {
        String originalMessage = "Hello, World!";
        byte[] secretKey1 = new byte[16];
        new Random().nextBytes(secretKey1); // generate a random AES key
        byte[] secretKey2 = new byte[16];
        new Random().nextBytes(secretKey2); // generate a different random AES key
        byte[] encryptedMessage = Encryption.encryptAES(originalMessage.getBytes(), secretKey1);
        assertThrows(Exception.class, () -> Encryption.decryptAES(encryptedMessage, secretKey2));
    }

    @Test
    void shouldFailEncryptionWithNullAESMessage() {
        byte[] secretKey = new byte[16];
        new Random().nextBytes(secretKey); // generate a random AES key
        assertThrows(Exception.class, () -> Encryption.encryptAES(null, secretKey));
    }

    @Test
    void shouldFailDecryptionWithNullAESMessage() {
        byte[] secretKey = new byte[16];
        new Random().nextBytes(secretKey); // generate a random AES key
        assertThrows(Exception.class, () -> Encryption.decryptAES(null, secretKey));
    }

    @Test
    void shouldFailEncryptionWithNullAESKey() {
        String originalMessage = "Hello, World!";
        assertThrows(Exception.class, () -> Encryption.encryptAES(originalMessage.getBytes(), null));
    }

    @Test
    void shouldFailDecryptionWithNullAESKey() {
        byte[] encryptedMessage = new byte[16];
        assertThrows(Exception.class, () -> Encryption.decryptAES(encryptedMessage, null));
    }

}
