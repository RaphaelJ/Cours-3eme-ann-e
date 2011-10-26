/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package status_client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Représente un client surveillant le status du serveur
 *
 * @author rapha
 */
public class StatusClient extends Thread {
    public static final byte PAUSE = (byte) 'P';
    public static final byte STOP = (byte) 'S';
    
    private String _ip;
    private int _port;
    
    public StatusClient(String ip, int port)
    {
        this._ip = ip;
        this._port = port;
    }
    
    @Override
    public void run()
    {
        try {
            Socket sock = new Socket(this._ip, this._port);
            InputStream in = sock.getInputStream();
            
            for (;;) {
                byte status = (byte) in.read();
                if (status == PAUSE) {
                    System.out.println("Serveur en pause");
                } else if (status == STOP) {
                    System.out.println("Serveur en cours d'arrêt");
                    System.exit(1);
                }
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(StatusClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(StatusClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
