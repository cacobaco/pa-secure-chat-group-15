package g15.pas.utils;

import java.io.Serializable;
import java.security.PublicKey;
import java.util.UUID;

/**
 * This class represents a Certificate which is used for secure communication.
 * It implements Serializable interface for easy transmission of its instances.
 */
public class Certificate implements Serializable {

    private final String serialNumber;
    private final String username;
    private final PublicKey publicKey;
    private Long expirationDate;
    private byte[] signature;

    /**
     * Constructor for creating a Certificate with a random serial number.
     *
     * @param username  the username associated with the certificate
     * @param publicKey the public key associated with the certificate
     */
    public Certificate(String username, PublicKey publicKey) {
        this.serialNumber = UUID.randomUUID().toString();
        this.username = username;
        this.publicKey = publicKey;
    }

    /**
     * Constructor for creating a Certificate with a specific serial number.
     *
     * @param serialNumber the serial number of the certificate
     * @param username     the username associated with the certificate
     * @param publicKey    the public key associated with the certificate
     */
    public Certificate(String serialNumber, String username, PublicKey publicKey) {
        this.serialNumber = serialNumber;
        this.username = username;
        this.publicKey = publicKey;
    }

    /**
     * @return the serial number of the certificate
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @return the username associated with the certificate
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the public key associated with the certificate
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @return the expiration date of the certificate
     */
    public Long getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the expiration date of the certificate.
     *
     * @param expirationDate the expiration date to be set
     */
    public void setExpirationDate(Long expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the signature of the certificate
     */
    public byte[] getSignature() {
        return signature;
    }

    /**
     * Sets the signature of the certificate.
     *
     * @param signature the signature to be set
     */
    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

}
