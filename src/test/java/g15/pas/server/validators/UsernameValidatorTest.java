package g15.pas.server.validators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UsernameValidatorTest {

    @Test
    void shouldValidateCorrectUsername() {
        assertTrue(UsernameValidator.isValid("valid_username"));
    }

    @Test
    void shouldInvalidateNullUsername() {
        assertFalse(UsernameValidator.isValid(null));
    }

    @Test
    void shouldInvalidateShortUsername() {
        assertFalse(UsernameValidator.isValid("1"));
    }

    @Test
    void shouldInvalidateLongUsername() {
        assertFalse(UsernameValidator.isValid("thisIsAVeryLongUsernameThatExceedsTheMaximumLength"));
    }

    @Test
    void shouldInvalidateUsernameWithInvalidCharacters() {
        assertFalse(UsernameValidator.isValid("invalid*username"));
    }

}
