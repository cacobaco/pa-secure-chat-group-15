package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.message.enums.CommandType;

/**
 * This class represents a command message that extends the Message class.
 * The message content is of type CommandType.
 * It provides constructors to create a CommandMessage with a command, sender, and recipients.
 */
public class CommandMessage extends Message<CommandType> {

    /**
     * Constructs a new CommandMessage with the specified command.
     * The sender and recipients are set to null.
     *
     * @param command the command of the message
     */
    public CommandMessage(CommandType command) {
        super(command);
    }

    /**
     * Constructs a new CommandMessage with the specified command and sender.
     * The recipients are set to null.
     *
     * @param command the command of the message
     * @param sender  the sender of the message
     */
    public CommandMessage(CommandType command, String sender) {
        super(command, sender);
    }

    /**
     * Constructs a new CommandMessage with the specified command, sender, and recipients.
     *
     * @param command    the command of the message
     * @param sender     the sender of the message
     * @param recipients the recipients of the message
     */
    public CommandMessage(CommandType command, String sender, String[] recipients) {
        super(command, sender, recipients);
    }

}
