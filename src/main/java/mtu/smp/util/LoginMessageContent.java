package mtu.smp.util;

import java.io.Serializable;

/**
 *
 * @author Guillermo Ruiz García
 */
public class LoginMessageContent extends MessageContent implements Serializable{ 
    private final String loginToken;
    private final String passToken;
    
    public LoginMessageContent(String login, String pass) {
        super(null);
        this.loginToken = login;
        this.passToken = pass;
    }
    
    public String getLoginToken() {
        return loginToken;
    }
    
    public String getPassToken() {
        return passToken;
    }
}
