package g15.pas.utils;

/**
 * This class provides utility methods for logging messages and errors.
 * It provides static methods to log messages to the standard output and errors to the standard error.
 */
public class Logger {

    /**
     * Logs a formatted message to the standard output.
     * The message is formatted using {@link String#format(String, Object...)}.
     *
     * @param message the format string
     * @param args    the arguments for the format string
     */
    public static void log(String message, Object... args) {
        System.out.printf(message + "\n", args);
    }

    /**
     * Logs a formatted error message to the standard output.
     * The error message is prefixed with "[ERRO]" and is formatted using {@link String#format(String, Object...)}.
     *
     * @param message the format string
     * @param args    the arguments for the format string
     */
    public static void error(String message, Object... args) {
        // System.err.printf(message + "\n", args); // TODO maybe remove, it's causing delay in messages
        System.out.printf("[ERRO] " + message + "\n", args);
    }

}
