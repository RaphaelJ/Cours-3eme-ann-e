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
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.jfree.data.statistics.Statistics;

/**
 *
 * @author rapha
 */
public class FreetaxStatsServer {
    public static void main(String args[])
            throws IOException, ClassNotFoundException, SQLException,
            InstantiationException, IllegalAccessException
    {
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
                         statsDesc(
                             con, obj_out, obj_in, (FreetaxStatsDesc) query
                         );
                    } else if (query instanceof FreetaxStats1D) {
                        
                    } else if (query instanceof FreetaxStats1DComp) {
                        
                    } else if (query instanceof FreetaxStats1DChron) {
                        
                    } else if (query instanceof FreetaxStats2DCorr) {
                        
                    } else if (query instanceof FreetaxStatsTestComp) {
                        
                    }
                }
            } else {
                // Identifiants non valides
                obj_out.writeObject(new FreetaxStatsFail());
                obj_out.flush();
            }
            
            sock.close();
        }
    }

    private static void statsDesc(Connection con, ObjectOutputStream obj_out,
            ObjectInputStream obj_in, FreetaxStatsDesc freetaxStatsDesc)
            throws SQLException, IOException
    {
        PreparedStatement instruc;
        int nbJours;
        
        if (freetaxStatsDesc.getSemaine() != null) {
            // Recherche par semaine
            instruc = con.prepareStatement(
                "SELECT DAYOFWEEK(v.date) AS jour, COUNT(*) AS count " +
                "FROM ventes AS v " +
                "INNER JOIN produits as p ON p.id = v.produit " +
                "WHERE p.categorie = ? " +
                "  AND EXTRACT(WEEK FROM v.date) = ? " +
                "GROUP BY EXTRACT(DAY FROM v.date);"
            );
            instruc.setInt(2, freetaxStatsDesc.getSemaine().intValue());
            nbJours = 7;
        } else {
            // Recherche par mois
            if (freetaxStatsDesc.getMois() == null) {
                // Sélectionne le mois précédent si aucun mois n'est renseigné
                Calendar cal = new GregorianCalendar();
                cal.add(Calendar.MONTH, -1);
                freetaxStatsDesc.setMois(cal.get(Calendar.MONTH));
            }
            
            instruc = con.prepareStatement(
                "SELECT DAYOFMONTH(v.date) AS jour, COUNT(*) AS count " +
                "FROM ventes AS v " +
                "INNER JOIN produits as p ON p.id = v.produit " +
                "WHERE p.categorie = ? " +
                "  AND EXTRACT(MONTH FROM v.date) = ? " +
                "GROUP BY EXTRACT(DAY FROM v.date) " +
                "ORDER BY "
            );
            instruc.setInt(2, freetaxStatsDesc.getMois().intValue());
            
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.MONTH, freetaxStatsDesc.getMois());
            nbJours = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        }
        instruc.setString(1, freetaxStatsDesc.getCategorie());
        ResultSet rs = instruc.executeQuery();
        
        int[] ventes = new int[nbJours];
        while (rs.next()) {
            ventes[rs.getInt("jour")] = rs.getInt("count");
        }
        
        Number[] ventesInteger = new Integer[nbJours];
        int i = 0;
        for (int v : ventes) {
            ventesInteger[i] = v;
            i++;
        }
        
        double moyenne = Statistics.calculateMean(ventesInteger);
        double ecartType = Statistics.getStdDev(ventesInteger);
        
        Integer mode = null;
        for (int v : ventes) {
            if (mode == null || v > mode.intValue())
                mode = v;
        }
        
        obj_out.writeObject(new FreetaxStatsDescReponse(
            ventes, moyenne, mode.intValue(), ecartType
        ));
        obj_out.flush();
    }
}
