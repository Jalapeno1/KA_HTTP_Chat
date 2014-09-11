package ka_http_chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClient extends Thread {
    

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    //private Scanner input;
    BufferedReader input;
    private PrintWriter output;
    List<ChatListener> listeners = new ArrayList<>();
    

    public void registerChatListener(ChatListener liste) {
        listeners.add(liste);
    }

    public void unRegisterEchoListener(ChatListener liste) {
        listeners.remove(liste);
    }

    private void notifyListeners(String msg) {
        for (ChatListener listener : listeners) {
            listener.messageReceived(msg);
        }
    }

    public void connect(String address, int port) throws UnknownHostException, IOException {
        this.port = port;
        serverAddress = InetAddress.getByName(address);
        socket = new Socket(serverAddress, port);
        Reader reader = new InputStreamReader(socket.getInputStream());
        input = new BufferedReader(reader);
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour      
        start();
    }

    public void send(String msg) {
        output.println(msg);
    }

    public void stopClient() throws IOException {
        output.println(ProtocolStrings.STOP);
    }

    public void run() {
        try {
            String msg = input.readLine();
            while (!msg.equals(ProtocolStrings.STOP)) {
                notifyListeners(msg);
                msg = input.readLine();
            }
            try {
                socket.close();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE,null,ex);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE,null,ex);
        }

    }
//        public static void main(String[] args)
//    {
//        int port = 9090;
//        String ip = "localhost";
//        if (args.length == 2)
//        {
//            port = Integer.parseInt(args[0]);
//            ip = args[1];
//        }
//        try
//        { 
//            TestListener dummy = new TestListener();
//            ChatClient tester = new ChatClient();
//            tester.registerChatListener(dummy);
//            tester.connect(ip, port);       
//            System.out.println("Sending 'Hello world'");
//            tester.send("Hello World");
//            System.out.println("Waiting for a reply");     
//            tester.stopClient();
//            System.in.read();      
//        } catch (UnknownHostException ex)
//        {
//            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (IOException ex)
//        {
//            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    

    public void connect(int port, String ip)
    {
        try
        { 
            TestListener dummy = new TestListener();
            ChatClient tester = new ChatClient();
            tester.registerChatListener(dummy);
            tester.connect(ip, port);       
            System.out.println("Sending 'Hello world'");
            tester.send("Hello World");
            System.out.println("Waiting for a reply");     
            tester.stopClient();
            System.in.read();      
        } catch (UnknownHostException ex)
        {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex)
        {
            java.util.logging.Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
        
}
