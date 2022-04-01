package mtu.smp.client;

import mtu.smp.util.MyStreamSocket;
import mtu.smp.util.Message;
import java.net.*;
import java.io.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * This class is a module which provides the application logic and the session layer for the SMP
 * client using stream-mode SSL Sockets.
 *
 * @author Guillermo Ruiz García
 */
public class SmpClientHelper {

    private final MyStreamSocket mySocket;
    private final InetAddress serverHost;
    private final int serverPort;

    public SmpClientHelper(String hostName, String portNum) throws SocketException,
            UnknownHostException, IOException {
        this.serverHost = InetAddress.getByName(hostName);
        this.serverPort = Integer.parseInt(portNum);
        SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        SSLSocket c = (SSLSocket) f.createSocket(serverHost, serverPort);
        c.startHandshake();
        System.out.println("Connection request made");
        this.mySocket = new MyStreamSocket(c);
    }

    public Message sendAndReceiveMessage(Message message) throws SocketException,
            IOException, ClassNotFoundException, SocketTimeoutException {
        Message response;
        mySocket.sendMessage(message);
        mySocket.getSocket().setSoTimeout(5000);
        response = mySocket.receiveMessage();
        return response;
    }

    public Message endConnection() throws SocketException, IOException, ClassNotFoundException {
        Message endMessage = new Message(Message.Type.LOGOFF, null);
        Message response = sendAndReceiveMessage(endMessage);
        mySocket.close();
        return response;
    }
}
