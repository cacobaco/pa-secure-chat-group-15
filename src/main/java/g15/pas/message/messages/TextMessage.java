package g15.pas.message.messages;

import g15.pas.message.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TextMessage extends Message<String> {

    public TextMessage(String content) {
        super(content);
    }

    public TextMessage(String content, String sender) {
        super(content, sender);
    }

    public TextMessage(String content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

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

    public void format() {
        Date date = new Date();

        if (getSender() == null) {
            setContent(String.format("[%s] %s", date, getContent()));
        } else {
            setContent(String.format("[%s] %s: %s", date, getSender(), getContent()));
        }
    }

}
