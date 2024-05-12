package g15.pas.utils;

import java.io.Serializable;
import java.security.PublicKey;

/**
 * This class represents a Certificate which implements Serializable.
 * It contains a username, a public key;
 */
public class Certificate implements Serializable {

    private static int SERIAL_NUMBER = 0;

    private final int serialNumber;
    private final String username;
    private final PublicKey publicKey;
    private byte[] signature;

    /**
     * Constructor for Certificate with username and publicKey.
     *
     * @param username  The username associated with the certificate
     * @param publicKey The public key associated with the certificate
     */
    public Certificate(String username, PublicKey publicKey) {
        this.serialNumber = SERIAL_NUMBER++;
        this.username = username;
        this.publicKey = publicKey;
    }

    /**
     * Constructor for Certificate with serialNumber, username and publicKey.
     *
     * @param serialNumber The serial number associated with the certificate
     * @param username     The username associated with the certificate
     * @param publicKey    The public key associated with the certificate
     */
    public Certificate(int serialNumber, String username, PublicKey publicKey) {
        this.serialNumber = serialNumber;
        this.username = username;
        this.publicKey = publicKey;
    }

    /**
     * Getter for serialNumber.
     *
     * @return The serial number associated with the certificate
     */
    public int getSerialNumber() {
        return serialNumber;
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

}
