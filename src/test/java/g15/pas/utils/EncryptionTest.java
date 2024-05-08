package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionTest {

    @Test
    void shouldGenerateValidKeyPair() throws NoSuchAlgorithmException {
        KeyPair keyPair = Encryption.generateKeyPair();
        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivate());
        assertNotNull(keyPair.getPublic());
    }

    @Test
    void shouldSuccessfullyEncryptAndDecryptMessage() throws Exception {
        KeyPair keyPair = Encryption.generateKeyPair();
        String originalMessage = "Hello, World!";
        byte[] encryptedMessage = Encryption.encrypt(originalMessage, keyPair.getPublic());
        String decryptedMessage = Encryption.decrypt(encryptedMessage, keyPair.getPrivate());
        assertEquals(originalMessage, decryptedMessage);
    }

    @Test
    void shouldFailDecryptionWithWrongKey() throws Exception {
        KeyPair keyPair1 = Encryption.generateKeyPair();
        KeyPair keyPair2 = Encryption.generateKeyPair();
        String originalMessage = "Hello, World!";
        byte[] encryptedMessage = Encryption.encrypt(originalMessage, keyPair1.getPublic());

        assertThrows(Exception.class, () -> Encryption.decrypt(encryptedMessage, keyPair2.getPrivate()));
    }

    @Test
    void shouldFailEncryptionWithNullMessage() throws NoSuchAlgorithmException {
        KeyPair keyPair = Encryption.generateKeyPair();
        assertThrows(Exception.class, () -> Encryption.encrypt(null, keyPair.getPublic()));
    }

    @Test
    void shouldFailDecryptionWithNullMessage() throws NoSuchAlgorithmException {
        KeyPair keyPair = Encryption.generateKeyPair();
        assertThrows(Exception.class, () -> Encryption.decrypt(null, keyPair.getPrivate()));
    }

}
