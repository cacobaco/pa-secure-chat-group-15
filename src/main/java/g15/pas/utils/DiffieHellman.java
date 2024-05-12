package g15.pas.utils;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * This class provides methods for implementing the Diffie-Hellman key exchange protocol.
 * It allows two parties to each generate a private-public key pair, exchange their public keys,
 * and then each compute a shared secret key.
 */
public class DiffieHellman {

    private static final int NUM_BITS = 128;
    private static final BigInteger N = new BigInteger("1289971646");
    private static final BigInteger G = new BigInteger("3");

    /**
     * Generates a private key for the Diffie-Hellman protocol.
     *
     * @return the generated private key
     * @throws NoSuchAlgorithmException if the specified algorithm is not available
     */
    public static BigInteger generatePrivateKey() throws NoSuchAlgorithmException {
        Random randomGenerator = SecureRandom.getInstance("SHA1PRNG");
        return new BigInteger(NUM_BITS, randomGenerator);
    }

    /**
     * Generates a public key for the Diffie-Hellman protocol.
     *
     * @param privateKey the private key
     * @return the generated public key
     */
    public static BigInteger generatePublicKey(BigInteger privateKey) {
        return G.modPow(privateKey, N);
    }

    /**
     * Computes the shared secret key using the other party's public key and own private key.
     *
     * @param publicKey  the other party's public key
     * @param privateKey the own private key
     * @return the computed shared secret key
     */
    public static BigInteger computeSecret(BigInteger publicKey, BigInteger privateKey) {
        return publicKey.modPow(privateKey, N);
    }

}
