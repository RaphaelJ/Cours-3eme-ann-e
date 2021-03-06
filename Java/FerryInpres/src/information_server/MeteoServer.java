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

public class MeteoServer {    
    private static Properties prop;
    
    public static void main(String args[])
            throws IOException, ClassNotFoundException
    {
        prop = new Properties();
        prop.load(new FileInputStream("ferryinpres.properties"));
        int METEO_PORT = Integer.parseInt(prop.getProperty("METEO_PORT"));
        System.out.println(METEO_PORT);
        
        ServerSocket server_sock = new ServerSocket(METEO_PORT);
        
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
            
            MeteoQueryProtocol query = (MeteoQueryProtocol) obj_in.readObject();
            
            MeteoResponseProtocol response = new MeteoResponseProtocol();
            MeteoResponseProtocol.Temps[] temps = {
                MeteoResponseProtocol.Temps.PLUIES, 
                MeteoResponseProtocol.Temps.AVERSES,
                MeteoResponseProtocol.Temps.ONDEES,
                MeteoResponseProtocol.Temps.COUVERT,
                MeteoResponseProtocol.Temps.SOLEIL,
                MeteoResponseProtocol.Temps.CHAUD,
                MeteoResponseProtocol.Temps.CANICULE
            };
            
            // Sélectionne une prévision météo aléatoire pour chaque 
            // demande.
            for (int jour : query.getJours()) {
                response.getResultats().put(
                    jour,
                    temps[(int) Math.floor(Math.random() * 7.0)]
                );
            }
            
            obj_out.writeObject(response);
            obj_out.flush();
            sock.close();
        }
    }
}