package g15.pas.utils;

public class Logger {

    public static void log(String message, Object... args) {
        System.out.printf(message + "\n", args);
    }

    public static void error(String message, Object... args) {
        // System.err.printf(message + "\n", args); // TODO maybe remove, it's causing delay in messages
        System.out.printf("[ERRO] " + message + "\n", args);
    }

}
