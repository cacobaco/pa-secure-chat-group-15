package g15.pas.message.messages;

import g15.pas.message.Message;
import g15.pas.message.enums.InfoType;

public class InfoMessage extends Message<InfoType> {

    public InfoMessage(InfoType content) {
        super(content);
    }

    public InfoMessage(InfoType content, String sender) {
        super(content, sender);
    }
    
}
