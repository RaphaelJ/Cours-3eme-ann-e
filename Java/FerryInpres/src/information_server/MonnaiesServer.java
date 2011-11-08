/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author rapha
 */
public class MonnaiesServer {
    public static void main(String args[])
            throws IOException, ClassNotFoundException
    {
        ServerSocket server_sock = new ServerSocket(Config.MONNAIES_PORT);
        
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            System.out.println("Nouveau client");
            ObjectOutputStream obj_out = new ObjectOutputStream(
                sock.getOutputStream()
            );
            ObjectInputStream obj_in = new ObjectInputStream(
                sock.getInputStream()
            );
            
            MonnaiesQueryProtocol query
                    = (MonnaiesQueryProtocol) obj_in.readObject();
            
            MonnaiesResponseProtocol response = new MonnaiesResponseProtocol();
            
            // Donne la valeur d'échange de chaque monnaie demandée
            for (String monnaie : query.getMonnaies()) {
                double change;
                
                if ("Livre sterling".equals(monnaie)) 
                    change = 0.86;
                else if ("Dollar américain".equals(monnaie))
                    change = 1.37;
                else if ("Yen japonais".equals(monnaie))
                    change = 107.12;
                else 
                    change = 0;
                    
                response.getMonnaies().put(monnaie, change);
            }
            
            obj_out.writeObject(response);
            obj_out.flush();
            sock.close();
        }
    }
}
