package g15.pas.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Constants {

    public static final String CONFIG_FILE_PATH = "project.config";

    public static final String ENCRYPTION_ALGORITHM = "RSA";
    public static final int ENCRYPTION_KEY_SIZE = 2048;
    public static final Charset ENCRYPTION_CHARSET = StandardCharsets.UTF_8;

}
