package g15.pas.message;

import java.io.Serializable;

/**
 * This is an abstract class for a Message that implements Serializable.
 * The Message class is generic and can accept any type that extends Serializable.
 * It has three properties: content, sender, and recipients.
 */
public abstract class Message<T extends Serializable> implements Serializable {

    private T content; // The content of the message
    private final String sender; // The sender of the message
    private String[] recipients; // The recipients of the message

    /**
     * Constructor for Message with only content.
     *
     * @param content The content of the message
     */
    public Message(T content) {
        this.content = content;
        this.sender = null;
        this.recipients = null;
    }

    /**
     * Constructor for Message with content and sender.
     *
     * @param content The content of the message
     * @param sender  The sender of the message
     */
    public Message(T content, String sender) {
        this.content = content;
        this.sender = sender;
        this.recipients = null;
    }

    /**
     * Constructor for Message with content, sender, and recipients.
     *
     * @param content    The content of the message
     * @param sender     The sender of the message
     * @param recipients The recipients of the message
     */
    public Message(T content, String sender, String[] recipients) {
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
    }

    /**
     * Setter for content.
     *
     * @param content The content to set
     */
    public void setContent(T content) {
        this.content = content;
    }

    /**
     * Getter for content.
     *
     * @return The content of the message
     */
    public T getContent() {
        return content;
    }

    /**
     * Getter for sender.
     *
     * @return The sender of the message
     */
    public String getSender() {
        return sender;
    }

    /**
     * Getter for recipients.
     *
     * @return The recipients of the message
     */
    public String[] getRecipients() {
        return recipients;
    }

    /**
     * Setter for recipients.
     *
     * @param recipients The recipients to set
     */
    public void setRecipients(String[] recipients) {
        this.recipients = recipients;
    }

}
