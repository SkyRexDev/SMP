package mtu.smp.util;

import java.io.Serializable;

/**
 *
 * @author Guillermo Ruiz García
 */
public class Message implements Serializable {

    private final Type type;
    private final MessageContent msgContent;

    public enum Type {
        LOGIN,
        UPLOAD,
        DOWNLOAD,
        LOGOFF,
        ERROR,
        OK
    }

    public Message(Type type, MessageContent msgContent) {
        this.type = type;
        this.msgContent = msgContent;
    }

    public Type getType() {
        return this.type;
    }

    public MessageContent getMessageContent() {
        return this.msgContent;
    }

    @Override
    public String toString() {
        if (this.msgContent == null) {
            return """
                   {
                   MESSAGE
                     Type: """ + this.type + "\n" + "  Content: " + "\n" + "}";
        }
        return """
               {
               MESSAGE
                 Type:  """ + this.type + "\n" + "  Content: " + this.msgContent.getContentToken() + "\n}";
    }
}
