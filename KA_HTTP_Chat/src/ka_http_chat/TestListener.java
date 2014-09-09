/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ka_http_chat;

/**
 *
 * @author Jonas
 */
public class TestListener implements ChatListener
{
    @Override
    public void messageReceived(String msg)
    {
        System.out.println("IT WORKED "+msg);
    }
}
