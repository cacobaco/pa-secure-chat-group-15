package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateRevokerTest {

    @Test
    public void shouldRevokeCertificate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            CertificateRevoker.revokeCertificate(certificate);

            assertTrue(CertificateRevoker.isRevoked(certificate));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotBeRevokedIfNotInCRL() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            assertFalse(CertificateRevoker.isRevoked(certificate));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldBeExpiredIfPastExpirationDate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            Long expirationDate = System.currentTimeMillis() - 100000;
            certificate.setExpirationDate(expirationDate);

            assertTrue(CertificateRevoker.isExpired(certificate));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldNotBeExpiredIfBeforeExpirationDate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            Long expirationDate = System.currentTimeMillis() + 100000;
            certificate.setExpirationDate(expirationDate);

            assertFalse(CertificateRevoker.isExpired(certificate));
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
