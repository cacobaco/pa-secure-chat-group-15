package g15.pas.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {

    private static final Properties properties = new Properties();

    public static final String SERVER_HOST;
    public static final int SERVER_PORT;
    public static final int MIN_USERNAME_LENGTH;
    public static final int MAX_USERNAME_LENGTH;
    public static final String USERNAME_REGEX;

    static {
        loadProperties();

        SERVER_HOST = properties.getProperty("server.host", "localhost");
        SERVER_PORT = Integer.parseUnsignedInt(properties.getProperty("server.port", "8000"));
        MIN_USERNAME_LENGTH = Integer.parseUnsignedInt(properties.getProperty("username.min.length", "3"));
        MAX_USERNAME_LENGTH = Integer.parseUnsignedInt(properties.getProperty("username.max.length", "20"));
        USERNAME_REGEX = properties.getProperty("username.regex", "^[a-zA-Z0-9_]+$");
    }

    private static void loadProperties() {
        try (InputStream inputStream = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Erro ao carregar o ficheiro de configuração: " + e.getMessage());
        }
    }

}
