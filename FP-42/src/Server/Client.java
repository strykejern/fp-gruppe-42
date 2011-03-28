/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.Inet4Address;
import java.net.SocketTimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author Snorre, Anders og Jan-Tore
 */
public class Client {

    private ConnectionImpl conn;

    public Client(){
        conn = new ConnectionImpl(201);
        try {
            conn.connect(Inet4Address.getByAddress(new byte[]{78, 91, 1, 111}), 2000);
        } catch (IOException ex) {

        }
    }

    public void sendMessage(String message){
        try {
            conn.send(message);
        } catch (ConnectException ex) {
        } catch (IOException ex) {
        }
    }

    public static void main(String[] args) {
        Client c = new Client();
        c.sendMessage("lol");
    }
}
