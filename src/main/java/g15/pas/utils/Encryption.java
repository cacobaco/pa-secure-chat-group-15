package g15.pas.utils;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;

public class Encryption {

    private static final String ALGORITHM = Constants.ENCRYPTION_ALGORITHM;
    private static final int KEY_SIZE = Constants.ENCRYPTION_KEY_SIZE;
    private static final Charset CHARSET = Constants.ENCRYPTION_CHARSET;

    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    public static byte[] encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] messageBytes = message.getBytes(CHARSET);

        return cipher.doFinal(messageBytes);
    }

    public static String decrypt(byte[] messageBytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedMessageBytes = cipher.doFinal(messageBytes);

        return new String(decryptedMessageBytes, CHARSET);
    }

}
