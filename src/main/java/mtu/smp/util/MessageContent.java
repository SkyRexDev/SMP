package mtu.smp.util;

import java.io.Serializable;

/**
 *
 * @author Guillermo Ruiz García
 */
public class MessageContent implements Serializable {
    private String contentToken;
    
    public MessageContent(String message) {
        this.contentToken = message;
    }
    
    public String getContentToken() {
        return this.contentToken;
    }
    
    public void setContentToken(String contentToken) {
        this.contentToken = contentToken;
    }
}
