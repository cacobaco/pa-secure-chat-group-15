package g15.pas.message.messages;

import g15.pas.message.enums.InfoType;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InfoMessageTest {

    @Test
    void testConstructorAndGetters() {
        InfoType content = InfoType.LOGOUT;
        String sender = "sender";

        InfoMessage message = new InfoMessage(content);

        assertEquals(content, message.getContent(), "O conteúdo deve ser igual");
        assertNull(message.getSender(), "O remetente deve ser nulo");

        message = new InfoMessage(content, sender);

        // Assert
        assertEquals(content, message.getContent(), "O conteúdo deve ser igual");
        assertEquals(sender, message.getSender(), "O remetente deve ser igual");
    }
}
