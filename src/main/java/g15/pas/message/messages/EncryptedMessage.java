package g15.pas.message.messages;

import g15.pas.message.Message;

/**
 * This class represents an encrypted message that extends the Message class.
 * The message content is of type byte array, which represents the encrypted message.
 * It also contains a MAC (Message Authentication Code) to ensure the integrity and authenticity of the message.
 * It provides a constructor to create an EncryptedMessage with an encrypted message, MAC, sender, and recipients.
 */
public class EncryptedMessage extends Message<byte[]> {

    private final byte[] mac;

    /**
     * Constructs a new EncryptedMessage with the specified encrypted message, MAC, sender, and recipients.
     *
     * @param encryptedMessage the encrypted message
     * @param mac              the MAC of the message
     * @param sender           the sender of the message
     * @param recipients       the recipients of the message
     */
    public EncryptedMessage(byte[] encryptedMessage, byte[] mac, String sender, String[] recipients) {
        super(encryptedMessage, sender, recipients);
        this.mac = mac;
    }

    /**
     * Returns the MAC of the message.
     *
     * @return the MAC of the message
     */
    public byte[] getMac() {
        return mac;
    }

}
