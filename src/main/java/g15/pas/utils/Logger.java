package g15.pas.utils;

/**
 * This class provides static methods for logging messages and errors.
 * It uses the standard output stream for logging messages and errors.
 */
public class Logger {

    /**
     * Logs a formatted message to the standard output stream.
     *
     * @param message The format string.
     * @param args    The arguments for the format string.
     */
    public static void log(String message, Object... args) {
        System.out.printf(message + "\n", args);
    }

    /**
     * Logs a formatted error message to the standard output stream.
     * The message is prefixed with "[ERRO]".
     *
     * @param message The format string.
     * @param args    The arguments for the format string.
     */
    public static void error(String message, Object... args) {
        // System.err.printf(message + "\n", args); // TODO maybe remove, it's causing delay in messages
        System.out.printf("[ERRO] " + message + "\n", args);
    }

}