package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;

public class CommandMessage extends Message<CommandType> {

    public CommandMessage(CommandType content) {
        super(content);
    }

    public CommandMessage(CommandType content, String sender) {
        super(content, sender);
    }

    public CommandMessage(CommandType content, String sender, String[] recipients) {
        super(content, sender, recipients);
    }

}
