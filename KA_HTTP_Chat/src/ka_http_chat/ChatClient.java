package ka_http_chat;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatClient extends Thread {

    Socket socket;
    private int port;
    private InetAddress serverAddress;
    private Scanner input;
    private PrintWriter output;
    List<ChatListener> listeners = new ArrayList<>();

    public void registerEchoListener(ChatListener liste) {
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
        input = new Scanner(socket.getInputStream());
        output = new PrintWriter(socket.getOutputStream(), true);  //Set to true, to get auto flush behaviour      
        start();
    }

    public void send(String msg) {
        output.println(msg);
    }


}