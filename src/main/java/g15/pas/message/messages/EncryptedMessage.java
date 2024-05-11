package g15.pas.message.messages;

import java.io.Serializable;
import java.util.List;

public class EncryptedMessage implements Serializable {
    private final byte[] encryptedMessage;
    private final byte[] signature;
    private final List<String> recipients;
    private final String sender;
    public EncryptedMessage(byte[] encryptedMessage, byte[] signature, List<String> recipients, String sender) {
        this.encryptedMessage = encryptedMessage;
        this.signature = signature;
        this.recipients = recipients;
        this.sender = sender;
    }
    public byte[] getEncryptedMessage() {
        return encryptedMessage;
    }
    public byte[] getSignature() {
        return signature;
    }
    public List<String> getRecipients() {
        return recipients;
    }
    public String getSender() {
        return sender;
    }
}