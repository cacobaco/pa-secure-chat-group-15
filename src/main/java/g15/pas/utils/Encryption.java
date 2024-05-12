package g15.pas.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.spec.X509EncodedKeySpec;

/**
 * This class provides utility methods for encryption and decryption using RSA and AES algorithms.
 */
public class Encryption {

    /**
     * Generates a RSA KeyPair.
     *
     * @return the generated KeyPair
     * @throws Exception if an error occurs during KeyPair generation
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Converts a byte array to a PublicKey.
     *
     * @param keyBytes the byte array to be converted
     * @return the generated PublicKey
     * @throws Exception if an error occurs during PublicKey generation
     */
    public static PublicKey convertBytesToPublicKey(byte[] keyBytes) throws Exception {
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * Encrypts a message using RSA.
     *
     * @param message   the message to be encrypted
     * @param publicKey the PublicKey to be used for encryption
     * @return the encrypted message
     * @throws Exception if an error occurs during encryption
     */
    public static byte[] encryptRSA(byte[] message, Key publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return cipher.doFinal(message);
    }

    /**
     * Decrypts a message using RSA.
     *
     * @param message    the message to be decrypted
     * @param privateKey the PrivateKey to be used for decryption
     * @return the decrypted message
     * @throws Exception if an error occurs during decryption
     */
    public static byte[] decryptRSA(byte[] message, Key privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(message);
    }

    /**
     * Encrypts a message using AES.
     *
     * @param message   the message to be encrypted
     * @param secretKey the secret key to be used for encryption
     * @return the encrypted message
     * @throws Exception if an error occurs during encryption
     */
    public static byte[] encryptAES(byte[] message, byte[] secretKey) throws Exception {
        byte[] secretKeyPadded = ByteBuffer.allocate(16).put(secretKey).array();
        SecretKeySpec secreteKeySpec = new SecretKeySpec(secretKeyPadded, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secreteKeySpec);
        return cipher.doFinal(message);
    }

    /**
     * Decrypts a message using AES.
     *
     * @param message   the message to be decrypted
     * @param secretKey the secret key to be used for decryption
     * @return the decrypted message
     * @throws Exception if an error occurs during decryption
     */
    public static byte[] decryptAES(byte[] message, byte[] secretKey) throws Exception {
        byte[] secretKeyPadded = ByteBuffer.allocate(16).put(secretKey).array();
        SecretKeySpec secreteKeySpec = new SecretKeySpec(secretKeyPadded, "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secreteKeySpec);
        return cipher.doFinal(message);
    }

}
