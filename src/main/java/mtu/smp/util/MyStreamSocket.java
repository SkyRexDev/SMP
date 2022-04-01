package mtu.smp.util;

import java.io.*;
import javax.net.ssl.SSLSocket;

/**
 * A wrapper class of Socket which contains methods for sending and receiving
 * messages
 *
 * @author Guillermo Ruiz García
 */
public class MyStreamSocket {

    private final SSLSocket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    
    public MyStreamSocket(SSLSocket socket) throws IOException {
        this.socket = socket;
        setStreams();
    }

    private void setStreams() throws IOException {
        OutputStream outStream = socket.getOutputStream();
        output = new ObjectOutputStream(outStream);
        InputStream inStream = socket.getInputStream();
        input = new ObjectInputStream(inStream);
    }

    public void sendMessage(Message message) throws IOException {
        output.writeObject(message);
    } 

    public Message receiveMessage() throws IOException, ClassNotFoundException {
        Message message = (Message) input.readObject();
        return message;
    }
    
    public SSLSocket getSocket() {
        return this.socket;
    }

    public void close() throws IOException {
        socket.close();
    }
}
