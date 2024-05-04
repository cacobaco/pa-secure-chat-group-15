package g15.pas.utils;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.*;

/**
 * This class provides utility methods for encryption and decryption.
 * It uses the encryption algorithm, key size, and charset defined in the Constants class.
 */
public class Encryption {

    private static final String ALGORITHM = Constants.ENCRYPTION_ALGORITHM;
    private static final int KEY_SIZE = Constants.ENCRYPTION_KEY_SIZE;
    private static final Charset CHARSET = Constants.ENCRYPTION_CHARSET;

    /**
     * Generates a KeyPair using the RSA algorithm and the key size defined in the Constants class.
     *
     * @return A KeyPair generated using the RSA algorithm.
     * @throws NoSuchAlgorithmException If the RSA algorithm is not available in the environment.
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Encrypts a message using a PublicKey.
     *
     * @param message   The message to be encrypted.
     * @param publicKey The PublicKey to be used for encryption.
     * @return The encrypted message as a byte array.
     * @throws Exception If an error occurs during encryption.
     */
    public static byte[] encrypt(String message, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] messageBytes = message.getBytes(CHARSET);

        return cipher.doFinal(messageBytes);
    }

    /**
     * Decrypts a message using a PrivateKey.
     *
     * @param messageBytes The encrypted message as a byte array.
     * @param privateKey   The PrivateKey to be used for decryption.
     * @return The decrypted message as a String.
     * @throws Exception If an error occurs during decryption.
     */
    public static String decrypt(byte[] messageBytes, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] decryptedMessageBytes = cipher.doFinal(messageBytes);

        return new String(decryptedMessageBytes, CHARSET);
    }

}
