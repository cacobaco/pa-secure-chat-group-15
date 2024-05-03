package g15.pas.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {

    private String content;
    private final String sender;
    private final String[] recipients;

    public Message(String content) {
        this.content = content;
        this.sender = null;
        this.recipients = null;
    }

    public Message(String content, String sender) {
        this.content = content;
        this.sender = sender;
        this.recipients = null;
    }

    public Message(String content, String sender, String[] recipients) {
        this.content = content;
        this.sender = sender;
        this.recipients = recipients;
    }

    public static Message fromString(String content, String sender) {
        if (!content.startsWith("@")) {
            return new Message(content, sender);
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
            return new Message(content, sender);
        }

        return new Message(content, sender, recipients.toArray(new String[0]));
    }

    public void format() {
        Date date = new Date();

        if (sender == null) {
            content = String.format("[%s] %s", date, content);
        } else {
            content = String.format("[%s] %s: %s", date, sender, content);
        }
    }

    public String getContent() {
        return content;
    }

    public String getSender() {
        return sender;
    }

    public String[] getRecipients() {
        return recipients;
    }

}
