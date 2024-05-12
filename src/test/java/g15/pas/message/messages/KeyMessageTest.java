package g15.pas.message.messages;

import org.junit.jupiter.api.Test;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;

class KeyMessageTest {

    @Test
    void testConstructorAndGetters() {
        Key key = null;

        KeyMessage message = new KeyMessage(key);

        assertEquals(key, message.getContent(), "A chave deve ser igual");
    }
}

