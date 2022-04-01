package mtu.smp.client;

import mtu.smp.util.LoginMessageContent;
import mtu.smp.util.Message;
import mtu.smp.util.MessageContent;
import java.io.*;
import java.net.SocketTimeoutException;

/**
 * This module contains the presentaton logic of the SMP Client
 *
 * @author Guillermo Ruiz García
 */
public class SmpClient {

    public static void main(String[] args) {
        System.setProperty("jdk.tls.server.protocols", "TLSv1.2");
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");
        System.setProperty("javax.net.ssl.trustStore", "public.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);
        try {
            System.out.print("""
                               Welcome to the SMP client.
                               What is the IP of the server host? 
                               [localhost by default]
                               IP: """);
            String hostName = br.readLine();
            if (hostName.length() == 0) {
                hostName = "localhost";
            }
            System.out.print("""
                               What is the port number of the server host?
                               [port 10000 by default]
                               Port: """);
            String portNum = br.readLine();
            if (portNum.length() == 0) {
                portNum = "10000";
            }
            SmpClientHelper helper = new SmpClientHelper(hostName, portNum);
            boolean done = false;
            boolean logged = false;
            String option, content;
            Message msg, response;
            MessageContent msgContent;
            do {
                System.out.print("User: ");
                String user = br.readLine();
                System.out.print("Password: ");
                String pass = br.readLine();
                msgContent = new LoginMessageContent(user, pass);
                msg = new Message(Message.Type.LOGIN, msgContent);
                response = helper.sendAndReceiveMessage(msg);
                if (response.getType() == Message.Type.OK) {
                    logged = true;
                    System.out.println(response.toString());
                    System.out.println("[Client]: You have successfully been loged in the server");
                    do {
                        System.out.println("""
                                           Select an option:
                                           0. LOGOFF
                                           1. UPLOAD
                                           2. DOWNLOAD
                                           """);
                        option = br.readLine();
                        switch (option) {
                            case "0":
                                response = helper.endConnection();
                                if (response.getType() == Message.Type.OK) {
                                    System.out.println(response.toString());
                                    System.out.println("[Client]: The connection has been closed successfully");
                                } else if (response.getType() == Message.Type.ERROR) {
                                    System.out.println("[Client]: The connection has been closed with an error");
                                }
                                done = true;
                                break;

                            case "1":
                                System.out.println("Type the message you want to upload: ");
                                content = br.readLine();
                                msg = new Message(Message.Type.UPLOAD, new MessageContent(content));
                                response = helper.sendAndReceiveMessage(msg);
                                if (response.getType() == Message.Type.OK) {
                                    System.out.println(response.toString());
                                    System.out.println("[Client]: Your message has been uploaded successfully");
                                } else if (response.getType() == Message.Type.ERROR) {
                                    System.out.println(response.toString());
                                    System.out.println("[Client]: Your message could not be uploaded");
                                }
                                break;

                            case "2":
                                msg = new Message(Message.Type.DOWNLOAD, null);
                                response = helper.sendAndReceiveMessage(msg);
                                if (response.getType() == Message.Type.OK) {
                                    System.out.println(response.toString());
                                    System.out.println("[Client]: All messages downloaded successfully");
                                } else if (response.getType() == Message.Type.ERROR) {
                                    System.out.println(response.toString());
                                    System.out.println("[Client]: An error has occured during the download");
                                }
                                break;

                            default:
                                System.out.println("The option MUST be a number between [0-2]");
                        }
                    } while (!done);
                } else if (response.getType() == Message.Type.ERROR) {
                    System.out.println("[Client]: An error has occured during the login process");
                    System.out.println(response.toString());
                }
            } while (!logged);
        } // end try  
        catch (SocketTimeoutException ex) {
            System.out.println("Timeout exception. Response not received in 5 seconds.");
        }
        catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        //end catch
    } //end main
} // end class
