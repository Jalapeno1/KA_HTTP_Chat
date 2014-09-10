/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import ka_http_chat.ChatClient;
import ka_http_chat.ChatServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Jonas
 */
public class TestClient
{

    public TestClient()
    {
    }

    @BeforeClass
    public static void setUpClass()
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    ChatServer.main(null);
                } catch (FileNotFoundException ex)
                {
                    Logger.getLogger(TestClient.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }).start();
    }

    @AfterClass
    public static void tearDownClass()
    {
        ChatServer.stopServer();
    }

    @Before
    public void setUp()
    {
    }

    @Test
    public void send() throws IOException
    {
        ChatClient client = new ChatClient();
        client.connect("localhost", 9090);
        client.send("Hello");
        //assertEquals("HELLO", client.receive);
    }
}
