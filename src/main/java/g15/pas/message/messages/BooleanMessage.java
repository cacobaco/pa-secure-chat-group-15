package g15.pas.message.messages;

import g15.pas.message.Message;

/**
 * This class extends the Message class with Boolean as the type parameter.
 * It represents a boolean message that can be sent from a sender to multiple recipients.
 * The content of a BooleanMessage is a Boolean, which represents the boolean value to be sent.
 */
public class BooleanMessage extends Message<Boolean> {

    /**
     * Constructor for BooleanMessage with only content.
     *
     * @param content The content of the message
     */
    public BooleanMessage(Boolean content) {
        super(content);
    }

    /**
     * Constructor for BooleanMessage with content and sender.
     *
     * @param content The content of the message
     * @param sender  The sender of the message
     */
    public BooleanMessage(Boolean content, String sender) {
        super(content, sender);
    }

    /**
     * Constructor for BooleanMessage with content, sender, and recipients.
     *
     * @param content    The content of the message
     * @param sender     The sender of the message
     * @param recipients The recipients of the message
     */
    public BooleanMessage(Boolean content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

}
