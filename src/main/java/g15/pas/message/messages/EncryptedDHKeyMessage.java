package g15.pas.message.messages;

import g15.pas.message.Message;

/**
 * This class represents an encrypted Diffie-Hellman key message that extends the Message class.
 * The message content is of type byte array, which represents the encrypted key.
 * It provides a constructor to create an EncryptedDHKeyMessage with an encrypted key, sender, and recipients.
 */
public class EncryptedDHKeyMessage extends Message<byte[]> {

    /**
     * Constructs a new EncryptedDHKeyMessage with the specified encrypted key, sender, and recipients.
     *
     * @param encryptedKey the encrypted Diffie-Hellman key of the message
     * @param sender       the sender of the message
     * @param recipients   the recipients of the message
     */
    public EncryptedDHKeyMessage(byte[] encryptedKey, String sender, String[] recipients) {
        super(encryptedKey, sender, recipients);
    }

}
