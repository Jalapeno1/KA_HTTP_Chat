/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ka_http_chat;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

/**
 *
 * @author Jonas
 */
public class HTTPServer
{
    static int port = 9090; 
    static String ip = "127.0.0.1";
    
    public static void runHTTPServer() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/html", new htmlHandler());
        server.createContext("/pdf", new pdfHandler());
        server.createContext("/log", new chatlog());
        server.setExecutor(null);
        server.start();
    }
    
    static class htmlHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            String answer = "Under Construction!";
            he.sendResponseHeaders(200, answer.length());
            OutputStream os = he.getResponseBody();
            os.write(answer.getBytes());
            os.close();
        }

    }
    
    static class chatlog implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            BufferedReader re = new BufferedReader(new FileReader(new File("chatLog.txt")));
            String line;
            
            StringBuilder sb = new StringBuilder();
            
            sb.append("<!DOCTYPE html>\n");
            sb.append("<html>\n");
            sb.append("<head>\n");
            sb.append("<title>ChatLog</title>\n");
            sb.append("<meta charset='UTF-8'>\n");
            sb.append("</head>\n");
            sb.append("<body>\n");
            
            while((line = re.readLine()) != null){
                String a = re.readLine();
                sb.append("<p>"+a+"</p>");
            }
            
            sb.append("</body>\n");
            sb.append("</html>\n");
            
            String answer = sb.toString();
            
            he.sendResponseHeaders(200, answer.length());
            OutputStream os = he.getResponseBody();
            os.write(answer.getBytes());
            os.close();
        }

    }
    
    static class pdfHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "application/pdf");
            
            File file = new File("CA-Chat.pdf");
            byte[] bytearray = new byte[(int) file.length()];
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            bis.read(bytearray, 0, bytearray.length);

            he.sendResponseHeaders(200, file.length());
            OutputStream os = he.getResponseBody();
            os.write(bytearray, 0, bytearray.length);
            os.close();
        }    
    }
    
//    public static void main(String[] args) throws IOException
//    {
//        runHTTPServer();
//    }
}
