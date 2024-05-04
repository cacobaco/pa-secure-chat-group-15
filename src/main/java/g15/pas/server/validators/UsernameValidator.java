package g15.pas.server.validators;

import g15.pas.utils.Config;

/**
 * This class provides a static method to validate usernames.
 * The validation rules are:
 * - The username must not be null.
 * - The length of the username must be between the minimum and maximum lengths defined in the Config class.
 * - The username must match the regular expression defined in the Config class.
 */
public class UsernameValidator {

    /**
     * Validates a username based on the rules defined in the Config class.
     *
     * @param username The username to be validated.
     * @return true if the username is valid, false otherwise.
     */
    public static boolean isValid(String username) {
        return username != null
                && username.length() >= Config.MIN_USERNAME_LENGTH
                && username.length() <= Config.MAX_USERNAME_LENGTH
                && username.matches(Config.USERNAME_REGEX);
    }

}
