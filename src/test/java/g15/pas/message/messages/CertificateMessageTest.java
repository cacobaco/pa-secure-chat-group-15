package g15.pas.message.messages;

import g15.pas.utils.Certificate;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CertificateMessageTest {

    private static final String USERNAME = "username";
    private static Certificate certificate;

    @BeforeAll
    static void setUp() {
        certificate = new Certificate(USERNAME, null);
    }

    @Test
    void shouldCreateMessageWithContentOnly() {
        CertificateMessage message = new CertificateMessage(certificate);
        assertEquals(certificate, message.getContent());
    }

    @Test
    void shouldCreateMessageWithContentAndSender() {
        CertificateMessage message = new CertificateMessage(certificate, "sender");
        assertEquals(certificate, message.getContent());
        assertEquals("sender", message.getSender());
    }

    @Test
    void shouldCreateMessageWithContentSenderAndRecipients() {
        String[] recipients = {"recipient1", "recipient2"};
        CertificateMessage message = new CertificateMessage(certificate, "sender", recipients);
        assertEquals(certificate, message.getContent());
        assertEquals("sender", message.getSender());
        assertArrayEquals(recipients, message.getRecipients());
    }

}
