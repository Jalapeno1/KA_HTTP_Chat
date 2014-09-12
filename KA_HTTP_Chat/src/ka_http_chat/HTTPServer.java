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
    static int port = 8888; 
    static String ip = "127.0.0.1";
    
    public static void runHTTPServer() throws IOException
    {
        HttpServer server = HttpServer.create(new InetSocketAddress(ip, port), 0);
        server.createContext("/dok", new docHandler());
        server.createContext("/main", new mainHandler());
        server.createContext("/pdf", new pdfHandler());
        server.createContext("/log", new chatlog());
        server.createContext("/client", new jarHandler());
        server.setExecutor(null);
        server.start();
    }
    
    static class mainHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            BufferedReader bRead = new BufferedReader(new FileReader(new File("html/main.html")));
            String line = bRead.readLine();
            
            StringBuilder sb = new StringBuilder();
            
            while(line != null){
                sb.append(line); 
                line = bRead.readLine();
            }
     
            String answer = sb.toString();
            he.sendResponseHeaders(200, answer.length());
            OutputStream os = he.getResponseBody();
            os.write(answer.getBytes());
            os.close();
        }
    }
    
    static class docHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "application/pdf");
            
            File file = new File("documentation.pdf");
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
    
    static class chatlog implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            BufferedReader re = new BufferedReader(new FileReader(new File("chatLog.txt")));           
            BufferedReader br1 = new BufferedReader(new FileReader(new File("html/log.html")));
            
            String htmlLine1 = br1.readLine();
            String line = re.readLine();
            
            StringBuilder sb = new StringBuilder();
            
            while(htmlLine1 != null){
                if (htmlLine1.contains("<!--startpoint-->")){ //builds a table of log data
                    while (line != null){
                        sb.append("<tr>");
                        sb.append("<td>"+line+"</td>");
                        line = re.readLine();
                        sb.append("<td>"+line+"</td>");
                        sb.append("</tr>");
                        line = re.readLine();
                    }
                }
                sb.append(htmlLine1);
                htmlLine1 = br1.readLine();
            }              
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
    
    static class jarHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange he) throws IOException
        {
            Headers h = he.getResponseHeaders();
            h.add("Content-Type", "application/jar");
            
            File file = new File("app.jar");
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
        
    public static void main(String[] args) throws IOException
    {
        runHTTPServer();
    }
}
