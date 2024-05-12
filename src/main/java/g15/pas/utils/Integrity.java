package g15.pas.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;

/**
 * This class provides utility methods for generating and verifying Message Authentication Codes (MACs) using the HmacSHA256 algorithm.
 */
public class Integrity {

    private static final String MAC_ALGORITHM = "HmacSHA256";

    /**
     * Generates a MAC for a given message using a given key.
     *
     * @param message the message for which the MAC is to be generated
     * @param macKey  the key to be used for MAC generation
     * @return the generated MAC
     * @throws Exception if an error occurs during MAC generation
     */
    public static byte[] generateMAC(byte[] message, byte[] macKey) throws Exception {
        SecretKeySpec secretKeySpec = new SecretKeySpec(macKey, MAC_ALGORITHM);
        Mac mac = Mac.getInstance(MAC_ALGORITHM);
        mac.init(secretKeySpec);
        return mac.doFinal(message);
    }

    /**
     * Verifies a MAC against a computed MAC.
     *
     * @param mac         the MAC to be verified
     * @param computedMac the computed MAC against which the MAC is to be verified
     * @return true if the MACs are equal, false otherwise
     */
    public static boolean verifyMAC(byte[] mac, byte[] computedMac) {
        return Arrays.equals(mac, computedMac);
    }

}
