package g15.pas.message.messages;

import g15.pas.message.Message;

public class EncryptedDHKeyMessage extends Message<byte[]> {
    public EncryptedDHKeyMessage(byte[] encryptedKey, String sender, String[] recipients) {
        super(encryptedKey, sender, recipients);
    }

}