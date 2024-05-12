package g15.pas.utils;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.Date;

/**
 * This class represents a Certificate which implements Serializable.
 * It contains a username, a public key;
 */
public class Certificate implements Serializable {

    private final String username;
    private final PublicKey publicKey;
    private Date expirationDate;
    private byte[] signature;

    /**
     * Constructor for Certificate with username and publicKey.
     *
     * @param username  The username associated with the certificate
     * @param publicKey The public key associated with the certificate
     */
    public Certificate(String username, PublicKey publicKey) {
        this.username = username;
        this.publicKey = publicKey;
    }

    public Certificate(String username, PublicKey publicKey, byte[] signature) {
        this.username = username;
        this.publicKey = publicKey;
        this.signature = signature;
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
     * Getter for signature.
     *
     * @return The signature associated with the certificate
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Setter for signature.
     *
     * @param signature The signature to be set
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    /**
     * Getter for expirationDate.
     *
     * @return The expiration date associated with the certificate
     */
    public Date getExpirationDate() {
        return expirationDate;
    }

    /**
     * Setter for expirationDate.
     *
     * @param expirationDate The expiration date to be set
     */
    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

}
