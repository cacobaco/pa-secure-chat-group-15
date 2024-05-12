package g15.pas.server.validators;

import g15.pas.utils.Config;

/**
 * This class provides a utility method for validating usernames.
 * It checks if the username is not null, if its length is within the specified limits,
 * and if it matches the specified regular expression.
 */
public class UsernameValidator {

    /**
     * Checks if a given username is valid.
     * A username is considered valid if it is not null, its length is within the specified limits,
     * and it matches the specified regular expression.
     *
     * @param username the username to be validated
     * @return true if the username is valid, false otherwise
     */
    public static boolean isValid(String username) {
        return username != null
                && username.length() >= Config.MIN_USERNAME_LENGTH
                && username.length() <= Config.MAX_USERNAME_LENGTH
                && username.matches(Config.USERNAME_REGEX);
    }

}
