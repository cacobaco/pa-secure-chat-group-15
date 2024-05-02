package g15.pas.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Message implements Serializable {

    private final String sender;
    private String message;
    private List<String> recipients;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
        this.recipients = null;
        parse();
        format();
    }

    private void parse() {
        if (!message.startsWith("@")) {
            return;
        }

        String[] parts = message.split(" ");
        recipients = new ArrayList<>();

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

        message = String.join(" ", Arrays.copyOfRange(parts, recipients.size(), parts.length));

        if (recipients.isEmpty()) {
            recipients = null;
        }
    }

    private void format() {
        Date date = new Date();
        message = "[" + date + "] " + sender + ": " + message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public List<String> getRecipients() {
        return recipients;
    }

}
