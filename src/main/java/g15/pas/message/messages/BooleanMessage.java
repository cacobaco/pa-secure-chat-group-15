package g15.pas.message.messages;

import g15.pas.message.Message;

/**
 * This class represents a boolean message that extends the Message class.
 * The message content is of type Boolean.
 * It provides constructors to create a BooleanMessage with content, sender, and recipients.
 */
public class BooleanMessage extends Message<Boolean> {

    /**
     * Constructs a new BooleanMessage with the specified content.
     * The sender and recipients are set to null.
     *
     * @param content the content of the message
     */
    public BooleanMessage(Boolean content) {
        super(content);
    }

    /**
     * Constructs a new BooleanMessage with the specified content and sender.
     * The recipients are set to null.
     *
     * @param content the content of the message
     * @param sender  the sender of the message
     */
    public BooleanMessage(Boolean content, String sender) {
        super(content, sender);
    }

    /**
     * Constructs a new BooleanMessage with the specified content, sender, and recipients.
     *
     * @param content    the content of the message
     * @param sender     the sender of the message
     * @param recipients the recipients of the message
     */
    public BooleanMessage(Boolean content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

}
