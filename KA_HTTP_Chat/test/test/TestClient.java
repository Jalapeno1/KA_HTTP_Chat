/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.io.IOException;
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
                ChatServer.main(null);
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
