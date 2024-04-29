package g15.pas.server.validators;

import g15.pas.utils.Config;

public class UsernameValidator {

    public static boolean isValid(String username) {
        return username != null
                && username.length() >= Config.MIN_USERNAME_LENGTH
                && username.length() <= Config.MAX_USERNAME_LENGTH
                && username.matches(Config.USERNAME_REGEX);
    }

}
