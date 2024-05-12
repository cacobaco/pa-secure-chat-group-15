package g15.pas.message;

import java.io.Serializable;

/**
 * This abstract class represents a message that can be sent between users.
 * The message contains content of type T, a sender, and an array of recipients.
 * The content, sender, and recipients can be set and retrieved using the provided methods.
 *
 * @param <T> the type of the content of the message
 */
public abstract class Message<T extends Serializable> implements Serializable {

    private T content;
    private final String sender;
    private String[] recipients;

    /**
     * Constructs a new Message with the specified content.
     * The sender and recipients are set to null.
     *
     * @param content the content of the message
     */
    public Message(T content) {
        this.content = content;
        this.sender = null;
        this.recipients = null;
    }

    /**
     * Constructs a new Message with the specified content and sender.
     * The recipients are set to null.
     *
     * @param content the content of the message
     * @param sender  the sender of the message
     */
    public Message(T content, String sender) {
        this.content = content;
        this.sender = sender;
        this.recipients = null;
    }

    /**
     * Constructs a new Message with the specified content, sender, and recipients.
     *
     * @param content    the content of the message
     * @param sender     the sender of the message
     * @param recipients the recipients of the message
     */
    public Message(T content, String sender, String[] recipients) {
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
    }

    /**
     * Sets the content of the message.
     *
     * @param content the new content of the message
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Returns the content of the message.
     *
     * @return the content of the message
     */
    public T getContent() {
        return content;
    }

    /**
     * Returns the sender of the message.
     *
     * @return the sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Returns the recipients of the message.
     *
     * @return the recipients of the message
     */
    public String[] getRecipients() {
        return recipients;
    }

    /**
     * Sets the recipients of the message.
     *
     * @param recipients the new recipients of the message
     */
    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

}
