package g15.pas.message.messages;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptedMessageTest {

    @Test
    void testConstructorAndGetters() {
        byte[] encryptedMessage = "encryptedMessage".getBytes();
        byte[] mac = "mac".getBytes();
        String sender = "sender";
        String[] recipients = {"recipient1", "recipient2"};

        EncryptedMessage message = new EncryptedMessage(encryptedMessage, mac, sender, recipients);

        assertArrayEquals(encryptedMessage, message.getContent(), "Os dados devem ser iguais");
        assertEquals(sender, message.getSender(), "O remetente deve ser igual");
        assertArrayEquals(recipients, message.getRecipients(), "Os destinatários devem ser iguais");
        assertArrayEquals(mac, message.getMac(), "O MAC deve ser igual");
    }
}
