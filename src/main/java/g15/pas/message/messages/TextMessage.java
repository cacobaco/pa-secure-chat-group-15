package g15.pas.message.messages;

import g15.pas.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * This class extends the Message class with String as the type parameter.
 * It represents a text message that can be sent from a sender to multiple recipients.
 */
public class TextMessage extends Message<String> {

    /**
     * Constructor for TextMessage with only content.
     *
     * @param content The content of the message
     */
    public TextMessage(String content) {
        super(content);
    }

    /**
     * Constructor for TextMessage with content and sender.
     *
     * @param content The content of the message
     * @param sender  The sender of the message
     */
    public TextMessage(String content, String sender) {
        super(content, sender);
    }

    /**
     * Constructor for TextMessage with content, sender, and recipients.
     *
     * @param content    The content of the message
     * @param sender     The sender of the message
     * @param recipients The recipients of the message
     */
    public TextMessage(String content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

    /**
     * This method creates a TextMessage from a string.
     * If the content starts with "@", it treats the following words as recipients until a word without "@" is found.
     *
     * @param content The content of the message
     * @param sender  The sender of the message
     * @return A new TextMessage instance
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
     * This method formats the content of the message by adding a timestamp and the sender's name (if present).
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
