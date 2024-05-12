package g15.pas.message.messages;

import g15.pas.message.Message;

import java.security.Key;

public class KeyMessage extends Message<Key> {

    public KeyMessage(Key key) {
        super(key);
    }
    
}
