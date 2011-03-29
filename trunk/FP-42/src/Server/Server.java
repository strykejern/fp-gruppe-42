/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Server;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import no.ntnu.fp.net.MessageListener;
import no.ntnu.fp.net.ReceiveWorker;
import no.ntnu.fp.net.admin.Log;
import no.ntnu.fp.net.co.Connection;
import no.ntnu.fp.net.co.ConnectionImpl;

/**
 *
 * @author Snorre, Anders og Jan-Tore
 */
public class Server implements MessageListener {
    private ArrayList<ConnectionImpl> listen;

    public Server(){
        listen = new ArrayList<ConnectionImpl>();
    }

    public void start(){
        ConnectionImpl connListener = new ConnectionImpl(2000);
        
        while(true){
            try {
                ConnectionImpl clientConnection = (ConnectionImpl)connListener.accept();

                if (clientConnection == null) continue;

                System.out.println("ping");

                ReceiveWorker backgroundListener = new ReceiveWorker(clientConnection);
                backgroundListener.addMessageListener(this);
                backgroundListener.start();

                listen.add(clientConnection);

            } catch (IOException e) {
            }

        }
    }

    public void messageReceived(String message) {
        System.out.println(message);
        /*for (ConnectionImpl receiver : listen){
            try {
                receiver.send(message);
            } catch (ConnectException ex) {
                listen.remove(receiver);
            } catch (IOException ex) {
            }
        }*/
    }

    public static void main(String[] args) {
        Log.setLogName("Server Log");
        Server server = new Server();
        server.start();
    }
}
