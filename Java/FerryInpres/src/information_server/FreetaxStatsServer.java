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
import java.security.Security;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 *
 * @author rapha
 */
public class FreetaxStatsServer {
    public static void main(String args[])
            throws IOException, ClassNotFoundException, SQLException
    {
        Security.addProvider(
            new org.bouncycastle.jce.provider.BouncyCastleProvider()
        );
        
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://"+Config.MYSQL_HOST+"/freetax";
        Connection con = DriverManager.getConnection(url, "ferryinpres", "pass");
        
        ServerSocket server_sock = new ServerSocket(Config.FREETAXSTATS_PORT);
        
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
            
            // Vérifie les identifiants de connexion
            FreetaxStatsLogin login = (FreetaxStatsLogin) obj_in.readObject();
            login.getLogin();
            
            // Extrait le mot de passe de la base de données
            PreparedStatement instruc = con.prepareStatement(
                "SELECT COUNT(*) as existe " +
                "FROM utilisateurs " +
                "WHERE login = ? AND hashMotDePasse = ?"
            );
            instruc.setString(1, login.getLogin());
            instruc.setString(2, login.getHashedPassword());
            ResultSet rs = instruc.executeQuery();
            rs.next();
            if (rs.getInt("existe") != 0) {
                // Identifiants valides
                obj_out.writeObject(new FreetaxStatsAck());
                obj_out.flush();
                
                for (;;) {
                    FreetaxStatsProtocol query 
                            = (FreetaxStatsLogin) obj_in.readObject();
                            
                    if (query instanceof FreetaxStatsDesc) {
                        
                    } else if (query instanceof FreetaxStats1D) {
                        
                    } else if (query instanceof FreetaxStats1DComp) {
                        
                    } else if (query instanceof )
                }
            } else {
                // Identifiants non valides
                obj_out.writeObject(new FreetaxStatsFail());
                obj_out.flush();
            }
            
            sock.close();
        }
    }
}
