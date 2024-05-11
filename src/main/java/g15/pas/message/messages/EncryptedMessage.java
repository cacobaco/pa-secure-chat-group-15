package g15.pas.message.messages;

import g15.pas.message.Message;

import java.util.Arrays;

public class EncryptedMessage extends Message<byte[]> {
    private final byte[] signature;

    public EncryptedMessage(byte[] encryptedMessage, byte[] signature, String sender, String[] recipients) {
        super(encryptedMessage, sender, recipients);
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }
}