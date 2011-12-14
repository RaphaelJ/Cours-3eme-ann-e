/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

/**
 *
 * @author rapha
 */
public class InternationalServer {
    public static void main(String[] args)
            throws KeyStoreException, FileNotFoundException, IOException,
            NoSuchAlgorithmException, CertificateException,
            UnrecoverableKeyException, KeyManagementException,
            ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://"+Config.MYSQL_HOST+"/frontier";
        Connection con = DriverManager.getConnection(url, "ferryinpres", "pass");
        
        KeyStore store = KeyStore.getInstance("JKS");
        store.load(
            new FileInputStream("keystore.jks"),
            "pwdpwd".toCharArray()
        );
        
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(store, "pwdpwd".toCharArray());
        
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(store);
        
        SSLContext context = SSLContext.getInstance("SSLv3");
        context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        
        SSLServerSocketFactory factory = context.getServerSocketFactory();
        
        SSLServerSocket server_sock = (SSLServerSocket) factory.createServerSocket(
            Config.INTERNATIONAL_PORT
        );
                 
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            SSLSocket sock = (SSLSocket) server_sock.accept();
            System.out.println("Nouveau client");
            
            ObjectOutputStream out = new ObjectOutputStream(
                sock.getOutputStream()
            );
            ObjectInputStream in = new ObjectInputStream(
                sock.getInputStream()
            );
            
            VerifId query = (VerifId) in.readObject();
            
            // Vérifie si la personne est valide
            PreparedStatement instruc = con.prepareStatement(
                "SELECT COUNT(*) AS existe " +
                "FROM voyageur_international " +
                "WHERE id_international = ? AND nom = ? AND prenom = ? " +
                "  AND nationalite = ?"
            );
            instruc.setInt(1, query.getClientNationalId());
            instruc.setString(2, query.getClientName());
            instruc.setString(3, query.getClientSurname());
            instruc.setString(4, query.getNationalite());
            
            ResultSet rs = instruc.executeQuery();
            rs.next();

            if (rs.getInt("existe") != 0) {
                out.writeObject(new Ack());
            } else {
                out.writeObject(new Fail());
            }
            out.flush();
            
            sock.close();
        }
    }
    
}
