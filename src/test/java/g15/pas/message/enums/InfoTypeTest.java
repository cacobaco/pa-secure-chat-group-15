package g15.pas.message.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InfoTypeTest {

    @Test
    void testEnumValue() {
        // Assert
        assertEquals("LOGOUT", InfoType.LOGOUT.name(), "O valor do enum deve ser 'LOGOUT'");
    }
}

