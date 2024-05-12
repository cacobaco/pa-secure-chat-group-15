package g15.pas.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;

import org.junit.Before;
import org.junit.Test;

public class CertificateTest {

    private Certificate certificate;
    private PublicKey publicKey;

    @Before
    public void setUp() throws NoSuchAlgorithmException {


        // Generate a key pair for testing
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        publicKey = keyPair.getPublic();

        // Create a Certificate instance for testing
        certificate = new Certificate("testUser", publicKey);
    }

    @Test
    public void testCertificateConstruction() {
        assertNotNull("Certificate should not be null", certificate);
    }

    @Test
    public void testGetSerialNumber() {
        assertEquals("Serial number should match", 4, certificate.getSerialNumber());
    }

    @Test
    public void testGetUsername() {
        assertEquals("Username should match", "testUser", certificate.getUsername());
    }

    @Test
    public void testGetPublicKey() {
        assertEquals("Public key should match", publicKey, certificate.getPublicKey());
    }

    @Test
    public void testGetSignatureInitiallyNull() {
        assertNull("Signature should be null initially", certificate.getSignature());
    }

    @Test
    public void testSetAndGetSignature() {
        byte[] signature = new byte[] { 0x01, 0x02, 0x03 };
        certificate.setSignature(signature);
        assertEquals("Signature should match", signature, certificate.getSignature());
    }
}