package g15.pas.utils;

import org.junit.jupiter.api.Test;
import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class EncryptionTest {

    @Test
    public void testRSAEncryptionDecryption() {
        try {
            KeyPair keyPair = Encryption.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            byte[] originalMessage = "Hello, RSA!".getBytes();

            byte[] encryptedMessage = Encryption.encryptRSA(originalMessage, publicKey);

            byte[] decryptedMessage = Encryption.decryptRSA(encryptedMessage, privateKey);

            assertArrayEquals(originalMessage, decryptedMessage, "A mensagem descriptografada não corresponde à mensagem original");

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testAESEncryptionDecryption() {
        try {
            byte[] secretKey = new byte[16];
            new Random().nextBytes(secretKey);

            byte[] originalMessage = "Hello, AES!".getBytes();

            byte[] encryptedMessage = Encryption.encryptAES(originalMessage, secretKey);

            byte[] decryptedMessage = Encryption.decryptAES(encryptedMessage, secretKey);

            assertArrayEquals(originalMessage, decryptedMessage, "A mensagem descriptografada não corresponde à mensagem original");

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testConvertBytesToPublicKey() {
        try {
            KeyPair keyPair = Encryption.generateKeyPair();
            PublicKey originalPublicKey = keyPair.getPublic();

            byte[] publicKeyBytes = originalPublicKey.getEncoded();

            PublicKey convertedPublicKey = Encryption.convertBytesToPublicKey(publicKeyBytes);

            assertEquals(originalPublicKey, convertedPublicKey, "A chave pública convertida não corresponde à original");

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

}

