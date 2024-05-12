package g15.pas.message.messages;

import g15.pas.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class represents a text message that extends the Message class.
 * The message content is of type String.
 * It provides methods to create a TextMessage from a string and to format the message content.
 */
public class TextMessage extends Message<String> {

    /**
     * Constructs a new TextMessage with the specified content.
     * The sender and recipients are set to null.
     *
     * @param content the content of the message
     */
    public TextMessage(String content) {
        super(content);
    }

    /**
     * Constructs a new TextMessage with the specified content and sender.
     * The recipients are set to null.
     *
     * @param content the content of the message
     * @param sender  the sender of the message
     */
    public TextMessage(String content, String sender) {
        super(content, sender);
    }

    /**
     * Constructs a new TextMessage with the specified content, sender, and recipients.
     *
     * @param content    the content of the message
     * @param sender     the sender of the message
     * @param recipients the recipients of the message
     */
    public TextMessage(String content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

    /**
     * Creates a new TextMessage from a string.
     * If the content starts with "@", it is split into parts and the recipients are extracted.
     * The remaining parts are joined to form the new content.
     *
     * @param content the content of the message
     * @param sender  the sender of the message
     * @return a new TextMessage with the specified content, sender, and recipients
     */
    public static TextMessage fromString(String content, String sender) {
        if (!content.startsWith("@")) {
            return new TextMessage(content, sender);
        }

        String[] parts = content.split(" ");
        List<String> recipients = new ArrayList<>();

        for (String part : parts) {
            if (!part.startsWith("@")) {
                break;
            }

            if (part.endsWith(",")) {
                recipients.add(part.substring(1, part.length() - 1));
                continue;
            }

            recipients.add(part.substring(1));
        }

        content = String.join(" ", Arrays.copyOfRange(parts, recipients.size(), parts.length));

        if (recipients.isEmpty()) {
            return new TextMessage(content, sender);
        }

        return new TextMessage(content, sender, recipients.toArray(new String[0]));
    }

    /**
     * Formats the content of the message.
     * If the sender is null, the content is prefixed with the current date.
     * If the sender is not null, the content is prefixed with the current date and the sender.
     */
    public void format() {
        Date date = new Date();

        if (getSender() == null) {
            setContent(String.format("[%s] %s", date, getContent()));
        } else {
            setContent(String.format("[%s] %s: %s", date, getSender(), getContent()));
        }
    }

}
