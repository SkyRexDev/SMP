package mtu.smp.server;

import mtu.smp.util.Message;
import mtu.smp.util.MessageContent;
import mtu.smp.util.MyStreamSocket;
import static mtu.smp.server.SmpServer.storedMessages;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtu.smp.util.LoginMessageContent;

/**
 * This module is to be used with a concurrent SMP Server. Its run method
 * carries out the logic of a client session.
 *
 * @author Guillermo Ruiz García
 */

class ServerThread implements Runnable {

    MyStreamSocket myDataSocket;

    ServerThread(MyStreamSocket myDataSocket) {
        this.myDataSocket = myDataSocket;
    }

    @Override
    public void run() {
        boolean done = false;
        Message message;
        MessageContent msgContent;
        
        try {
            while (!done) {
                message = myDataSocket.receiveMessage();
                System.out.println("[Server]: " + message.getType() + " message has been received");
                message.toString();
                switch (message.getType()) {
                     case LOGIN:
                        LoginMessageContent lm = (LoginMessageContent) message.getMessageContent();
                        if(lm.getLoginToken().equals(SmpServer.user) && lm.getPassToken().equals(SmpServer.pass)) {
                            msgContent = new MessageContent("Login succesfull");
                            message = new Message(Message.Type.OK, msgContent);
                            myDataSocket.sendMessage(message);
                        }
                        else {
                            msgContent = new MessageContent("Login and/or password are incorrect");
                            message = new Message(Message.Type.ERROR, msgContent);
                            myDataSocket.sendMessage(message);
                        }
                        break;
                        
                    case LOGOFF:
                        System.out.println("Session over");
                        msgContent = new MessageContent("Session closed succesfully");
                        message = new Message(Message.Type.OK, msgContent);
                        myDataSocket.sendMessage(message);
                        myDataSocket.close();
                        done = true;
                        break;
                        
                    case UPLOAD:
                        storedMessages.add(message);
                        msgContent = new MessageContent(message.getMessageContent().getContentToken());
                        message = new Message(Message.Type.OK, msgContent);
                        myDataSocket.sendMessage(message);
                        System.out.println(storedMessages.get(storedMessages.size() - 1).toString());
                        break;
                    
                    case DOWNLOAD:
                        
                        if (storedMessages.isEmpty()) {
                            message = new Message(Message.Type.OK, null);
                            myDataSocket.sendMessage(message);
                        }
                        else {
                            String allMessages = "";
                            Message m;
                            int i;
                            for (i = 0; i < storedMessages.size() - 1; i++) {
                                m = storedMessages.get(i);
                                allMessages += m.getMessageContent().getContentToken() + ", ";
                            }
                            m = storedMessages.get(i);
                            allMessages += m.getMessageContent().getContentToken();
                            msgContent = new MessageContent(allMessages);
                            message = new Message(Message.Type.OK, msgContent);
                            myDataSocket.sendMessage(message);
                        }
                        break;
                        
                    default:
                        System.out.println("Not implemented yet");
                }   
            } //end while !done
        }// end try// end try
        catch (IOException | ClassNotFoundException ex) {
            msgContent = new MessageContent("[SERVER ERROR]: " + "\n" + ex.toString());
            message = new Message(Message.Type.ERROR, msgContent);
            try {
                myDataSocket.sendMessage(message);
            } catch (IOException ex1) {
                Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex1);
            }
        } // end catch
    } //end run
} //end class 
