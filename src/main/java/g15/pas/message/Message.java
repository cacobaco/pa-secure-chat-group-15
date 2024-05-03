package g15.pas.message;

import java.io.Serializable;

public abstract class Message<T extends Serializable> implements Serializable {

    private T content;
    private final String sender;
    private final String[] recipients;

    public Message(T content) {
        this.content = content;
        this.sender = null;
        this.recipients = null;
    }

    public Message(T content, String sender) {
        this.content = content;
        this.sender = sender;
        this.recipients = null;
    }

    public Message(T content, String sender, String[] recipients) {
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String[] getRecipients() {
        return recipients;
    }

}
