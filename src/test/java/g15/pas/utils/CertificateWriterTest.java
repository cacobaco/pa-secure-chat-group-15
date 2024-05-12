package g15.pas.utils;

import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;
import static org.junit.jupiter.api.Assertions.*;

public class CertificateWriterTest {

    @Test
    public void testWriteCertificate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            String filePath = "path/to/your/certificate.pem";

            CertificateWriter.writeCertificate(certificate, filePath);

            Path path = Paths.get(filePath);
            assertTrue(Files.exists(path), "O arquivo de certificado não foi criado");


            Files.deleteIfExists(Paths.get(filePath));
        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testConvertToPEM() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            String pemContent = CertificateWriter.convertToPEM(certificate);


            assertNotNull(pemContent);
            assertFalse(pemContent.isEmpty());

            assertTrue(pemContent.startsWith("-----BEGIN CERTIFICATE-----"));
            assertTrue(pemContent.endsWith("-----END CERTIFICATE-----"));

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testConvertToPEMWithSignature() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);
            certificate.setSignature(new byte[]{1, 2, 3});

            String pemContent = CertificateWriter.convertToPEM(certificate);


            assertNotNull(pemContent);
            assertFalse(pemContent.isEmpty());

            assertTrue(pemContent.startsWith("-----BEGIN CERTIFICATE-----"));
            assertTrue(pemContent.endsWith("-----END CERTIFICATE-----"));

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

}
