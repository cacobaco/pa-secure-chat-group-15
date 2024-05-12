package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.message.enums.InfoType;

/**
 * This class represents an information message that extends the Message class.
 * The message content is of type InfoType.
 * It provides constructors to create an InfoMessage with content and sender.
 */
public class InfoMessage extends Message<InfoType> {

    /**
     * Constructs a new InfoMessage with the specified content.
     * The sender is set to null.
     *
     * @param content the content of the message
     */
    public InfoMessage(InfoType content) {
        super(content);
    }

    /**
     * Constructs a new InfoMessage with the specified content and sender.
     *
     * @param content the content of the message
     * @param sender  the sender of the message
     */
    public InfoMessage(InfoType content, String sender) {
        super(content, sender);
    }

}
