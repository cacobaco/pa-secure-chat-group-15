package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;

/**
 * This class extends the Message class with CommandType as the type parameter.
 * It represents a command message that can be sent from a sender to multiple recipients.
 * The content of a CommandMessage is a CommandType, which represents the type of command.
 */
public class CommandMessage extends Message<CommandType> {

    /**
     * Constructor for CommandMessage with only content.
     *
     * @param command The content of the message
     */
    public CommandMessage(CommandType command) {
        super(command);
    }

    /**
     * Constructor for CommandMessage with content and sender.
     *
     * @param command The content of the message
     * @param sender  The sender of the message
     */
    public CommandMessage(CommandType command, String sender) {
        super(command, sender);
    }

    /**
     * Constructor for CommandMessage with content, sender, and recipients.
     *
     * @param command    The content of the message
     * @param sender     The sender of the message
     * @param recipients The recipients of the message
     */
    public CommandMessage(CommandType command, String sender, String[] recipients) {
        super(command, sender, recipients);
    }

}
