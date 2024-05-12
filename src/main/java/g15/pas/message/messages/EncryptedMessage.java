package g15.pas.message.messages;

import g15.pas.message.Message;

public class EncryptedMessage extends Message<byte[]> {
    private final byte[] mac;

    public EncryptedMessage(byte[] encryptedMessage, byte[] mac, String sender, String[] recipients) {
        super(encryptedMessage, sender, recipients);
        this.mac = mac;
    }

    public byte[] getMac() {
        return mac;
    }
}