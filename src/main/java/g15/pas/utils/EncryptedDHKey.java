package g15.pas.utils;

import java.io.Serializable;

public class EncryptedDHKey implements Serializable {
    private final byte[] encryptedKey;
    private final String[] recipients;
    private final String sender;

    public EncryptedDHKey(byte[] encryptedKey, String sender, String[] recipients) {
        this.encryptedKey = encryptedKey;
        this.recipients = recipients;
        this.sender = sender;
    }

    public byte[] getEncryptedKey() {
        return encryptedKey;
    }

    public String[] getRecipients() {
        return recipients;
    }

    public String getSender() {
        return sender;
    }
}