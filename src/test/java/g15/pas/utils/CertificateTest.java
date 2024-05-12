package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateTest {

    @Test
    public void shouldCreateCertificateWithRandomSerialNumber() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            assertNotNull(certificate.getSerialNumber());
            assertEquals(username, certificate.getUsername());
            assertEquals(publicKey, certificate.getPublicKey());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldCreateCertificateWithSpecificSerialNumber() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            String serialNumber = "1234567890";
            Certificate certificate = new Certificate(serialNumber, username, publicKey);

            assertEquals(serialNumber, certificate.getSerialNumber());
            assertEquals(username, certificate.getUsername());
            assertEquals(publicKey, certificate.getPublicKey());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldSetAndGetExpirationDate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            Long expirationDate = System.currentTimeMillis() + 100000;
            certificate.setExpirationDate(expirationDate);

            assertEquals(expirationDate, certificate.getExpirationDate());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldSetAndGetSignature() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            byte[] signature = new byte[]{1, 2, 3, 4, 5};
            certificate.setSignature(signature);

            assertArrayEquals(signature, certificate.getSignature());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
