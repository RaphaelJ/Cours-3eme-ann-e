package FreeTax;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Properties;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author rapha
 */
public class Database {
    public static Connection con = null;
    
    public static Connection connect() throws FileNotFoundException, IOException,
            InstantiationException, IllegalAccessException, 
            ClassNotFoundException, SQLException
    {
        if (con == null) {
            String MYSQL_HOST = "127.0.0.1";

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            String url = "jdbc:mysql://"+MYSQL_HOST+"/freetax";
            con = DriverManager.getConnection(url, "ferryinpres", "pass");
        }
            
        return con;
    }    
    
    public static String[] listerLangues() throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT nom FROM langues"
        );
        
        ResultSet rs = instruc.executeQuery();
        
        LinkedList<String> list = new LinkedList<String>();
        while (rs.next())
            list.add(rs.getString(1));
        
        return list.toArray(new String[0]);
    }
    
    public static Produit[] listerProduits() throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT id, categorie, nom, prixPackage, uniteMonetaire "+
            "FROM produits "+
            "WHERE quantitePackage > 0"
        );
        
        ResultSet rs = instruc.executeQuery();
        
        LinkedList<Produit> list = new LinkedList<Produit>();
        while (rs.next()) {
            list.add(new Produit(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getDouble(4), rs.getString(5)
            ));
        }
        
        return list.toArray(new Produit[0]);
    }
    
    public static Produit[] listerProduits(int code_reserv) throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT p.id, p.categorie, p.nom, p.prixPackage, p.uniteMonetaire " +
            "FROM produits AS p " +
            "INNER JOIN caddie_produits AS cp " +
            "    ON cp.produit = p.id " +
            "WHERE cp.code_reserv = ?;"
        );
        instruc.setInt(1, code_reserv);
        ResultSet rs = instruc.executeQuery();
        
        LinkedList<Produit> list = new LinkedList<Produit>();
        while (rs.next()) {
            list.add(new Produit(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getDouble(4), rs.getString(5)
            ));
        }
        
        return list.toArray(new Produit[0]);
    }
    
    public static Produit[] listerProduitsCommandes(int code_reserv) throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT p.id, p.categorie, p.nom, p.prixPackage, p.uniteMonetaire " +
            "FROM produits AS p " +
            "INNER JOIN commande_produits AS cp " +
            "    ON cp.produit = p.id " +
            "WHERE cp.code_reserv = ?;"
        );
        instruc.setInt(1, code_reserv);
        ResultSet rs = instruc.executeQuery();
        
        LinkedList<Produit> list = new LinkedList<Produit>();
        while (rs.next()) {
            list.add(new Produit(
                rs.getInt(1), rs.getString(2), rs.getString(3),
                rs.getDouble(4), rs.getString(5)
            ));
        }
        
        return list.toArray(new Produit[0]);
    }
    
    public static boolean produitDispo(int code_produit) throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT quantitePackage FROM produits WHERE id = ?;"
        );
        instruc.setInt(1, code_produit);
        ResultSet rs = instruc.executeQuery();
        
        rs.next();
        
        return rs.getInt(1) > 0;
    }

    static void ajoutProduit(int code_reserv, int code_produit) 
            throws Exception
    {
        connect();

        // Réduit le stock
        PreparedStatement instruc = con.prepareStatement(
            "UPDATE produits SET quantitePackage = quantitePackage - 1 " +
            "WHERE id = ?;"
        );
        
        instruc.setInt(1, code_produit);
        instruc.execute();
        
        // Ajoute au panier
        instruc = con.prepareStatement(
            "INSERT INTO caddie_produits (produit, code_reserv) " +
            "VALUES (?, ?);"
        );
        
        instruc.setInt(1, code_produit);
        instruc.setInt(2, code_reserv);
        instruc.execute();
    }

    static void enleverProduit(int code_reserv, int code_produit) 
            throws Exception
    {
        connect();

        // Ré-augmente le stock
        PreparedStatement instruc = con.prepareStatement(
            "UPDATE produits SET quantitePackage = quantitePackage + 1 " +
            "WHERE id = ?;"
        );
        
        instruc.setInt(1, code_produit);
        instruc.execute();
        
        // Supprime du panier
        instruc = con.prepareStatement(
            "DELETE FROM caddie_produits " +
            "WHERE produit = ? AND code_reserv = ?;"
        );
        
        instruc.setInt(1, code_produit);
        instruc.setInt(2, code_reserv);
        instruc.execute();
    }
    
    static boolean compteValide(int compte) throws Exception
    {
        connect();
        
        PreparedStatement instruc = con.prepareStatement(
            "SELECT COUNT(*) FROM cartes_bancaires WHERE id = ?;"
        );
        instruc.setInt(1, compte);
        ResultSet rs = instruc.executeQuery();
        
        rs.next();
        
        return rs.getInt(1) > 0;
    }

    static void commander(int code_reserv)
            throws Exception
    {
        connect();
        
        // Ajoute la commande
        PreparedStatement instruc = con.prepareStatement(
            "INSERT INTO commande_produits " +
            "    SELECT null, produit, code_reserv " +
            "    FROM caddie_produits WHERE code_reserv = ?;"
        );
        instruc.setInt(1, code_reserv);
        instruc.execute();
        
        // Supprime du panier
        instruc = con.prepareStatement(
            "DELETE FROM caddie_produits " +
            "WHERE code_reserv = ?;"
        );
        
        instruc.setInt(1, code_reserv);
        instruc.execute();
    }
}
