package g15.pas.message.messages;

import g15.pas.message.Message;

import java.security.Key;

/**
 * This class represents a key message that extends the Message class.
 * The message content is of type Key.
 * It provides a constructor to create a KeyMessage with a key.
 */
public class KeyMessage extends Message<Key> {

    /**
     * Constructs a new KeyMessage with the specified key.
     * The sender and recipients are set to null.
     *
     * @param key the key of the message
     */
    public KeyMessage(Key key) {
        super(key);
    }

}
