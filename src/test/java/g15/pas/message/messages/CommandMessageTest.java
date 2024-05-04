package g15.pas.message.messages;

import g15.pas.message.enums.CommandType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CommandMessageTest {

    @Test
    void shouldCreateMessageWithContentOnly() {
        CommandMessage message = new CommandMessage(CommandType.REQUEST_CERTIFICATE);
        assertEquals(CommandType.REQUEST_CERTIFICATE, message.getContent());
    }

    @Test
    void shouldCreateMessageWithContentAndSender() {
        CommandMessage message = new CommandMessage(CommandType.REQUEST_CERTIFICATE, "sender");
        assertEquals(CommandType.REQUEST_CERTIFICATE, message.getContent());
        assertEquals("sender", message.getSender());
    }

    @Test
    void shouldCreateMessageWithContentSenderAndRecipients() {
        String[] recipients = {"recipient1", "recipient2"};
        CommandMessage message = new CommandMessage(CommandType.REQUEST_CERTIFICATE, "sender", recipients);
        assertEquals(CommandType.REQUEST_CERTIFICATE, message.getContent());
        assertEquals("sender", message.getSender());
        assertArrayEquals(recipients, message.getRecipients());
    }

}
