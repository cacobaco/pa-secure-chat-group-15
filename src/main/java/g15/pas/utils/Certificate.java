package g15.pas.utils;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents a Certificate which implements Serializable.
 * It contains a username, a public key, and a map for additional information.
 */
public class Certificate implements Serializable {

    private final String username;
    private final PublicKey publicKey;
    private final Map<String, String> additionalInfo;

    /**
     * Constructor for Certificate with username and publicKey.
     *
     * @param username  The username associated with the certificate
     * @param publicKey The public key associated with the certificate
     */
    public Certificate(String username, PublicKey publicKey) {
        this.username = username;
        this.publicKey = publicKey;
        this.additionalInfo = new HashMap<>();
    }

    /**
     * Getter for username.
     *
     * @return The username associated with the certificate
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for publicKey.
     *
     * @return The public key associated with the certificate
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Getter for additionalInfo.
     *
     * @return The map containing additional information associated with the certificate
     */
    public Map<String, String> getAdditionalInfo() {
        return additionalInfo;
    }

    /**
     * Method to add additional information to the certificate.
     *
     * @param key   The key for the additional information
     * @param value The value for the additional information
     */
    public void addAdditionalInfo(String key, String value) {
        this.additionalInfo.put(key, value);
    }

}
