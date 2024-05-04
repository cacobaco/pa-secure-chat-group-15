package g15.pas.message.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class BooleanMessageTest {

    @Test
    void shouldCreateMessageWithContentOnly() {
        BooleanMessage message = new BooleanMessage(true);
        assertEquals(true, message.getContent());
    }

    @Test
    void shouldCreateMessageWithContentAndSender() {
        BooleanMessage message = new BooleanMessage(true, "sender");
        assertEquals(true, message.getContent());
        assertEquals("sender", message.getSender());
    }

    @Test
    void shouldCreateMessageWithContentSenderAndRecipients() {
        String[] recipients = {"recipient1", "recipient2"};
        BooleanMessage message = new BooleanMessage(true, "sender", recipients);
        assertEquals(true, message.getContent());
        assertEquals("sender", message.getSender());
        assertArrayEquals(recipients, message.getRecipients());
    }

}
