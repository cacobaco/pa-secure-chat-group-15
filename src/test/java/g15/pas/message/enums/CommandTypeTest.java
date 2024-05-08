package g15.pas.message.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandTypeTest {

    @Test
    void shouldReturnCorrectEnumValue() {
        assertEquals(CommandType.REQUEST_CERTIFICATE, CommandType.valueOf("REQUEST_CERTIFICATE"));
    }

    @Test
    void shouldThrowExceptionForInvalidValue() {
        assertThrows(IllegalArgumentException.class, () -> CommandType.valueOf("INVALID_VALUE"));
    }

}
