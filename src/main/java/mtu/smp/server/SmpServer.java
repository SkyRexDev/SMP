package mtu.smp.server;

import java.io.FileInputStream;
import mtu.smp.util.Message;
import mtu.smp.util.MyStreamSocket;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

/**
 * This module contains the application logic of an echo server which uses a
 * stream-mode socket for interprocess communication. Unlike EchoServer2, this
 * server services clients concurrently. A command-line argument is required to
 * specify the server port.
 *
 * @author Guillermo Ruiz García
 */
public class SmpServer {

    static List<Message> storedMessages;
    static String user = "Guillermo";
    static String pass = "123";
    static String ksName = "herong.jks";
    static char ksPassword[] = "password".toCharArray();
    static char ctPassword[] = "password".toCharArray();    
    
    public static void main(String[] args) {
        
        System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        
        int serverPort = 10000;
        if (args.length == 1) {
            serverPort = Integer.parseInt(args[0]);
        }
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream(ksName), ksPassword);
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, ctPassword);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket s = (SSLServerSocket) ssf.createServerSocket(serverPort);
            System.out.println("SMP server ready.");
            storedMessages = new ArrayList<>();
            while (true) {
                System.out.println("Waiting for a connection.");
                MyStreamSocket myDataSocket = new MyStreamSocket((SSLSocket) s.accept());
                System.out.println("Connection accepted");
                Thread theThread = new Thread(new ServerThread(myDataSocket));
                theThread.start();
            } 
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        } 
    } 
} 
