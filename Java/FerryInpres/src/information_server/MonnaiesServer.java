/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author rapha
 */
public class MonnaiesServer {
    private static Properties prop;
    
    public static void main(String args[])
            throws IOException, ClassNotFoundException
    {
        prop = new Properties();
        prop.load(new FileInputStream("ferryinpres.properties"));
        
        int MONNAIES_PORT = Integer.parseInt(prop.getProperty("MONNAIES_PORT"));
        System.out.println(MONNAIES_PORT);
        ServerSocket server_sock = new ServerSocket(MONNAIES_PORT);
        
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
