package g15.pas.utils;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;

import static org.junit.jupiter.api.Assertions.*;

public class CertificateReaderTest {

    @Test
    public void shouldReadCertificateFromFile() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            String serialNumber = "1234567890";
            Certificate certificate = new Certificate(serialNumber, username, publicKey);

            String filePath = "secure/tests/test_certificate.pem";

            CertificateWriter.writeCertificate(certificate, filePath);

            Certificate readCertificate = CertificateReader.readCertificate(filePath);

            assertEquals(serialNumber, readCertificate.getSerialNumber());
            assertEquals(username, readCertificate.getUsername());
            assertEquals(publicKey, readCertificate.getPublicKey());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldReadCertificateWithExpirationDateFromFile() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            String serialNumber = "1234567890";
            Certificate certificate = new Certificate(serialNumber, username, publicKey);

            Long expirationDate = System.currentTimeMillis() + 100000;
            certificate.setExpirationDate(expirationDate);

            String filePath = "secure/tests/test_certificate.pem";

            CertificateWriter.writeCertificate(certificate, filePath);

            Certificate readCertificate = CertificateReader.readCertificate(filePath);

            assertEquals(serialNumber, readCertificate.getSerialNumber());
            assertEquals(username, readCertificate.getUsername());
            assertEquals(publicKey, readCertificate.getPublicKey());
            assertEquals(expirationDate, readCertificate.getExpirationDate());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void shouldReadCertificateWithSignatureFromFile() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            String serialNumber = "1234567890";

            Certificate certificate = new Certificate(serialNumber, username, publicKey);
            certificate = CertificateSigner.signCertificate(certificate, keyPair.getPrivate());

            String filePath = "secure/tests/test_certificate.pem";

            CertificateWriter.writeCertificate(certificate, filePath);

            Certificate readCertificate = CertificateReader.readCertificate(filePath);

            assertEquals(serialNumber, readCertificate.getSerialNumber());
            assertEquals(username, readCertificate.getUsername());
            assertEquals(publicKey, readCertificate.getPublicKey());
            assertNotNull(readCertificate.getSignature());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

}
