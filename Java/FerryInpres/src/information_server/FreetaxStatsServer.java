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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.jdbc.JDBCPieDataset;
import org.jfree.data.jdbc.JDBCXYDataset;
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
            
            System.out.println("Pass: "+ login.getHashedPassword());
            
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
                            = (FreetaxStatsProtocol) obj_in.readObject();
                    
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
    
    private static int moisPrec()
    {
        // Sélectionne le mois précédent si aucun mois n'est renseigné
        Calendar cal = new GregorianCalendar();
        // cal.add(Calendar.MONTH, -1);
        return cal.get(Calendar.MONTH);
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
                "SELECT jour_semaine AS jour, count " +
                "FROM stats_desc " +
                "WHERE categorie = ? " +
                "  AND semaine = ?"
            );
            instruc.setInt(2, freetaxStatsDesc.getSemaine().intValue());
            nbJours = 7;
        } else {
            // Recherche par mois
            if (freetaxStatsDesc.getMois() == null) {
                // Sélectionne le mois précédent si aucun mois n'est renseigné
                freetaxStatsDesc.setMois(moisPrec());
            }
            
            instruc = con.prepareStatement(
                "SELECT jour_mois AS jour, count " +
                "FROM stats_desc " +
                "WHERE categorie = ? " +
                "  AND mois = ?"
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
            ventes[rs.getInt("jour")-1] = rs.getInt("count");
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
    
    private static void stats1D(Connection con, ObjectOutputStream obj_out,
            ObjectInputStream obj_in, FreetaxStats1D freetaxStats1D)
            throws SQLException, IOException
    {        
        String instruc;
        
        if (freetaxStats1D.getSemaine() != null) {
            // Recherche par semaine
            instruc = 
                "SELECT jour_semaine AS jour, count " +
                "FROM stats_desc " +
                "WHERE categorie = '"+ freetaxStats1D.getCategorie() +"' " +
                "  AND semaine = " + freetaxStats1D.getSemaine();
        } else {
            // Recherche par mois
            if (freetaxStats1D.getMois() == null) {
                // Sélectionne le mois précédent si aucun mois n'est renseigné
                freetaxStats1D.setMois(moisPrec());
            }
            
            instruc = 
                "SELECT jour_mois AS jour, count " +
                "FROM stats_desc " +
                "WHERE categorie = '" + freetaxStats1D.getCategorie() + "'" +
                "  AND mois = "+ freetaxStats1D.getMois();
        }
        
        if (freetaxStats1D.getType() == FreetaxStats1D.SECTORIEL) {
            JDBCPieDataset ds = new JDBCPieDataset(con);
            ds.executeQuery(instruc);
            
            obj_out.writeObject(ChartFactory.createPieChart(
                "Vente de produits par jour", ds, true, true, true
            ));
        } else {
            JDBCXYDataset ds = new JDBCXYDataset(con);
            ds.executeQuery(instruc);
            
            obj_out.writeObject(ChartFactory.createHistogram(
                "Vente de produits par jour", "Jour", "Ventes", ds,
                PlotOrientation.VERTICAL, true, true, true
            ));
        }
        obj_out.flush();
    }
}
