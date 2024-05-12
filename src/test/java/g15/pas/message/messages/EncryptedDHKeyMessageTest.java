package g15.pas.message.messages;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EncryptedDHKeyMessageTest {

    @Test
    void testConstructorAndGetters() {
        byte[] encryptedKey = "encryptedKey".getBytes();
        String sender = "sender";
        String[] recipients = {"recipient1", "recipient2"};

        EncryptedDHKeyMessage message = new EncryptedDHKeyMessage(encryptedKey, sender, recipients);

        assertArrayEquals(encryptedKey, message.getContent(), "Os dados devem ser iguais");
        assertEquals(sender, message.getSender(), "O remetente deve ser igual");
        assertArrayEquals(recipients, message.getRecipients(), "Os destinatários devem ser iguais");
    }
}
