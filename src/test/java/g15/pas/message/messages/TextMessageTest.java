package g15.pas.message.messages;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextMessageTest {

    @Test
    void shouldCreateMessageWithContentOnly() {
        TextMessage message = new TextMessage("Hello");
        assertEquals("Hello", message.getContent());
    }

    @Test
    void shouldCreateMessageWithContentAndSender() {
        TextMessage message = new TextMessage("Hello", "sender");
        assertEquals("Hello", message.getContent());
        assertEquals("sender", message.getSender());
    }

    @Test
    void shouldCreateMessageWithContentSenderAndRecipients() {
        String[] recipients = {"recipient1", "recipient2"};
        TextMessage message = new TextMessage("Hello", "sender", recipients);
        assertEquals("Hello", message.getContent());
        assertEquals("sender", message.getSender());
        assertArrayEquals(recipients, message.getRecipients());
    }

    @Test
    void shouldCreateMessageFromStringWithoutRecipients() {
        TextMessage message = TextMessage.fromString("Hello", "sender");
        assertEquals("Hello", message.getContent());
        assertEquals("sender", message.getSender());
        assertNull(message.getRecipients());
    }

    @Test
    void shouldCreateMessageFromStringWithRecipients() {
        TextMessage message = TextMessage.fromString("@recipient1, @recipient2 Hello", "sender");
        assertEquals("Hello", message.getContent());
        assertEquals("sender", message.getSender());
        assertArrayEquals(new String[]{"recipient1", "recipient2"}, message.getRecipients());
    }

    @Test
    void shouldFormatMessageWithoutSender() {
        TextMessage message = new TextMessage("Hello");
        message.format();
        assertTrue(message.getContent().startsWith("["));
        assertTrue(message.getContent().contains("] Hello"));
    }

    @Test
    void shouldFormatMessageWithSender() {
        TextMessage message = new TextMessage("Hello", "sender");
        message.format();
        assertTrue(message.getContent().startsWith("["));
        assertTrue(message.getContent().contains("] sender: Hello"));
    }

}
