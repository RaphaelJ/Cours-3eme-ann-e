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
import java.util.LinkedList;
import java.util.Properties;

/**
 *
 * @author rapha
 */
public class FreetaxServer {
    private static Properties prop;
    public static void main(String args[])
            throws IOException, ClassNotFoundException
    {        
        prop = new Properties();
        prop.load(new FileInputStream("ferryinpres.properties"));
        
        int FREETAX_PORT = Integer.parseInt(prop.getProperty("FREETAX_PORT"));
        System.out.println(FREETAX_PORT);
        
        ServerSocket server_sock = new ServerSocket(FREETAX_PORT);
        
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
            
            FreetaxQueryProtocol query
                    = (FreetaxQueryProtocol) obj_in.readObject();
            
            // Ajoute des produits pour chaque catégorie demandée
            LinkedList<FreetaxProduit> produits
                    = new LinkedList<FreetaxProduit>();
            
            // Ajoute des alcools
            if (query.isAlcools()) {
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.ALCOOLS,
                        "Vin rouge", "75 cl", "43€"
                    )
                );
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.ALCOOLS,
                        "Martini", "20 cl", "17€"
                    )
                );
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.ALCOOLS,
                        "Lait alcoolisé", "1 litre", "1€"
                    )
                );
            }
            
            if (query.isParfums()) {
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.PARFUMS,
                        "Parfum toilettes", "10 litres", "3€"
                    )
                );
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.PARFUMS,
                        "D&G", "10 cl", "100€"
                    )
                );
            }
            
            if (query.isTabacs()) {
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.TABACS,
                        "Marlboro", "10 paquets", "30€"
                    )
                );
                produits.addFirst(
                    new FreetaxProduit(
                        FreetaxProduit.Categories.TABACS,
                        "Camel", "5 paquets", "17€"
                    )
                );
            }
            
            FreetaxResponseProtocol response = new FreetaxResponseProtocol(
                produits.toArray(new FreetaxProduit[0])
            );
            
            obj_out.writeObject(response);
            obj_out.flush();
            sock.close();
        }
    }
}