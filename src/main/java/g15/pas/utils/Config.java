package g15.pas.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * This class provides a centralized location for configuration properties.
 * It reads properties from a file and provides static access to these properties.
 */
public class Config {

    private static final Properties properties = new Properties();

    public static final String SERVER_HOST;
    public static final int SERVER_PORT;

    public static final int MIN_USERNAME_LENGTH;
    public static final int MAX_USERNAME_LENGTH;
    public static final String USERNAME_REGEX;

    public static final String CA_HOST;
    public static final int CA_PORT;
    public static final String CA_PATH;
    public static final String CA_CRL_PATH;
    public static long CA_CERTIFICATE_VALIDITY;

    static {
        loadProperties();

        SERVER_HOST = properties.getProperty("server.host", "localhost");
        SERVER_PORT = Integer.parseUnsignedInt(properties.getProperty("server.port", "8000"));

        MIN_USERNAME_LENGTH = Integer.parseUnsignedInt(properties.getProperty("username.min.length", "3"));
        MAX_USERNAME_LENGTH = Integer.parseUnsignedInt(properties.getProperty("username.max.length", "20"));
        USERNAME_REGEX = properties.getProperty("username.regex", "^[a-zA-Z0-9_]+$");

        CA_HOST = properties.getProperty("ca.host", "localhost");
        CA_PORT = Integer.parseUnsignedInt(properties.getProperty("ca.port", "8100"));
        CA_PATH = properties.getProperty("ca.path", "secure/certificates/");
        CA_CRL_PATH = properties.getProperty("ca.crl.path", "secure/crl/");
        CA_CERTIFICATE_VALIDITY = Long.parseUnsignedLong(properties.getProperty("ca.certificate.validity", "1"));
    }

    /**
     * Loads properties from a file.
     * If an error occurs during loading, it logs the error message.
     */
    private static void loadProperties() {
        try (InputStream inputStream = new FileInputStream(Constants.CONFIG_FILE_PATH)) {
            properties.load(inputStream);
        } catch (IOException e) {
            Logger.error("Ocorreu um erro ao carregar o ficheiro de configuração: " + e.getMessage());
        }
    }

}
