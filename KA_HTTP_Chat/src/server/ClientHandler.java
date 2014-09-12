package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import ka_http_chat.ChatServer;
import ka_http_chat.ProtocolStrings;

public class ClientHandler extends Thread {

    Scanner input;
    PrintWriter writer;
    Socket socket;
    ChatServer server;

    public ClientHandler(Socket socket, ChatServer s) throws IOException {
        this.server = s;
        this.socket = socket;
        input = new Scanner(socket.getInputStream());
        writer = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        String message = input.nextLine(); //IMPORTANT blocking call
        String[] protocolParts = message.split("#");
        java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message));
        while (!message.equals(ProtocolStrings.STOP)) {
            if (protocolParts[0].equals("CONNECT")) {
                server.addNewClient(protocolParts[1], this);
            }
            if (protocolParts[0].equals("SEND")) {
                server.addMessage(protocolParts[2], this);
            }
            if (protocolParts[0].equals("CLOSE")) {
                server.removeClient(protocolParts[1]);
            }

            java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, String.format("Received the message: %1$S ", message.toUpperCase()));
            message = input.nextLine(); //IMPORTANT blocking call
            //protocolParts = message.split("#");
        }
        writer.println(ProtocolStrings.STOP);//Echo the stop message back to the client for a nice closedown
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        java.util.logging.Logger.getLogger(ChatServer.class.getName()).log(Level.INFO, "Closed a Connection");
    }

    public void send(String msg) {
        writer.println(msg);
    }
}
