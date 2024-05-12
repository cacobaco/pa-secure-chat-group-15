package g15.pas.utils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import static org.junit.jupiter.api.Assertions.*;

public class CertificateSignerTest {

    @Test
    public void testSignAndVerifyCertificate() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            String username = "example_user";
            Certificate certificate = new Certificate(username, publicKey);

            Certificate signedCertificate = CertificateSigner.signCertificate(certificate, privateKey);

            boolean verificationResult = CertificateSigner.verifyCertificate(signedCertificate, publicKey);

            assertTrue(verificationResult, "A verificação da assinatura falhou");

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }
    @Test
    public void testSerializeCertificate() {
        try {
            String username = "example_user";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            Certificate certificate = new Certificate(username, publicKey);

            byte[] serializedCertificate = CertificateSigner.serializeCertificate(certificate);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(serializedCertificate);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            String deserializedUsername = (String) objectInputStream.readObject();
            PublicKey deserializedPublicKey = (PublicKey) objectInputStream.readObject();
            objectInputStream.close();

            assertEquals(username, deserializedUsername, "O nome de usuário desserializado não corresponde");
            assertEquals(publicKey, deserializedPublicKey, "A chave pública desserializada não corresponde");

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }

    @Test
    public void testHashCertificate() {
        try {
            String username = "example_user";
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            Certificate certificate = new Certificate(username, publicKey);

            byte[] serializedCertificate = CertificateSigner.serializeCertificate(certificate);

            byte[] hash = CertificateSigner.hashCertificate(serializedCertificate);

            assertNotNull(hash);
            assertNotEquals(0, hash.length);

        } catch (Exception e) {
            fail("Exceção lançada: " + e.getMessage());
        }
    }
}
