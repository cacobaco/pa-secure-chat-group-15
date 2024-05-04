package g15.pas.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CertificateTest {

    @Test
    void shouldCreateCertificateWithUsernameAndPublicKey() {
        Certificate certificate = new Certificate("username", null);
        assertEquals("username", certificate.getUsername());
        assertNull(certificate.getPublicKey());
    }

    @Test
    void shouldAddAdditionalInfoToCertificate() {
        Certificate certificate = new Certificate("username", null);
        certificate.addAdditionalInfo("key", "value");
        assertEquals("value", certificate.getAdditionalInfo().get("key"));
    }

    @Test
    void shouldReturnEmptyAdditionalInfoIfNotSet() {
        Certificate certificate = new Certificate("username", null);
        assertTrue(certificate.getAdditionalInfo().isEmpty());
    }

}
