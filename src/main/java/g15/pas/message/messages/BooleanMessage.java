package g15.pas.message.messages;

import g15.pas.message.Message;

public class BooleanMessage extends Message<Boolean> {

    public BooleanMessage(Boolean content) {
        super(content);
    }

    public BooleanMessage(Boolean content, String sender) {
        super(content, sender);
    }

    public BooleanMessage(Boolean content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

}
