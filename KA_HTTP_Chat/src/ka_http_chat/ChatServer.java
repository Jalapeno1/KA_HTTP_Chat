/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ka_http_chat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.logging.Level;
import server.ClientHandler;

/**
 *
 * @author Jonas
 */
public class ChatServer
{

    private static boolean keepRunning = true;
    private static ServerSocket serverSocket;
    private static final Properties properties = Logger.initProperties("server.properties");
    private Map<String, ClientHandler> users = new HashMap();
    private Map<String, ClientHandler> MessageRecieved = new HashMap();
   
    
    
    public static void stopServer()
    {
        keepRunning = false;
    }
    
    public void addNewClient(String userName, ClientHandler ch){
        users.put(userName, ch);
        String message = "ONLINE#";
        for (String name : users.keySet())
        {
            message += name + ",";
        }
        for (ClientHandler h : users.values())
        {
            h.send(message);
        }
    }
    
    public void addMessage (String userName, String messageInput, ClientHandler ch1){
        MessageRecieved.put(messageInput, ch1);
        String notify = "MESSAGE#"+userName;
        for (String name : MessageRecieved.keySet()){
            notify += name + ":";
        }
        for (ClientHandler h1 : MessageRecieved.values()) 
        {
               h1.send(notify);
    }
    }
    
    public void removeClient (String name){
        users.remove(name);
    }

//    private static void handleClient(Socket socket) throws IOException
//    {
//        Scanner input = new Scanner(socket.getInputStream());
//        PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
//        String message = input.nextLine(); //IMPORTANT blocking call
//        java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
//        while (!message.equals(ProtocolStrings.STOP))
//        {
//            writer.println(message.toUpperCase());
//            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
//            message = input.nextLine(); //IMPORTANT blocking call
//        }
//        writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
//        socket.close();
//        java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
//    }
    
    
    
    public static void main(String[] args) throws FileNotFoundException, IOException
    {
        new ChatServer().HandleClient();
    }

    private void HandleClient() throws NumberFormatException, IOException {
        HTTPServer.runHTTPServer();
        int port = Integer.parseInt(properties.getProperty("port"));
        String ip = properties.getProperty("serverIp");
        String logFile = properties.getProperty("logFile");

//        Logger.setLogFile(logFile, ChatServer.class.getName());
       
        Logger.getLogger(logFile,ChatServer.class.getName()).log(Level.INFO, "Sever started");
        try
        {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(ip, port));
            do
            {            
                Socket socket = serverSocket.accept(); //Important Blocking call
                java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Connected to a client");
                new ClientHandler(socket, this).start();                
            } while (keepRunning);
        } catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "" );
        }     
        Logger.closeLogger(ChatServer.class.getName());
    }
}
