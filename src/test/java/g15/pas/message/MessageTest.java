package g15.pas.message;

import g15.pas.message.messages.InfoMessage;
import g15.pas.message.enums.InfoType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    @Test
    void testInfoMessageConstructorAndGetters() {
        InfoType content = InfoType.LOGOUT;
        String sender = "sender";
        String[] recipients = {"recipient1", "recipient2"};

        InfoMessage message1 = new InfoMessage(content);
        InfoMessage message2 = new InfoMessage(content, sender);

        assertNull(message1.getSender());
        assertEquals(sender, message2.getSender());
        assertNull(message1.getRecipients());
        assertNull(message2.getRecipients());
        assertEquals(content, message1.getContent());
        assertEquals(content, message2.getContent());
    }

    @Test
    void testInfoMessageSetterAndGetters() {
        InfoMessage message = new InfoMessage(null);

        String[] recipients = {"recipient1", "recipient2"};
        message.setRecipients(recipients);

        assertArrayEquals(recipients, message.getRecipients());
    }
}
