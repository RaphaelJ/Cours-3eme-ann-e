/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

import data_bean.DatabaseBean;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author rapha
 */
public class MobileCompanyServer {
    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException
    {
        ServerSocket server_sock = new ServerSocket(Config.MOBILE_COMPANY_PORT);
        
        DatabaseBean data = new DatabaseBean();
        System.out.println("Connecté à la base de données");
        
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            System.out.println("Nouveau client");
            
            new MobileServerThread(sock, data).start();
        }
    }
}

class MobileServerThread extends Thread {
    private Socket _sock;
    private DatabaseBean _data;
    
    public MobileServerThread(Socket sock, DatabaseBean data)
    {
        this._sock = sock;
        this._data = data;
    }
    
    @Override
    public void run()
    {
        try {
            DataInputStream in = new DataInputStream(
                this._sock.getInputStream()
            );
            DataOutputStream out = new DataOutputStream(
                this._sock.getOutputStream()
            );
            
            String numeroCarte = in.readUTF();
            
            if (this._data.utilisateurAuthorise(numeroCarte)) {
                out.writeChar('A');
                out.flush();
                
                System.out.println("Agent identifié: " + numeroCarte);
                this.manageClient(in, out);
            } else {
                out.writeChar('F');
                out.flush();
               
                System.out.println("Echec d'authentification agent : " + numeroCarte);
            }
            
            this._sock.close();
        } catch (Exception ex) {
            Logger.getLogger(MobileServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void manageClient(DataInputStream in, DataOutputStream out)
    {
        // Obtient l'immatriculation et la nationalité
        for (;;) {
            try {
                String immatriculation = in.readUTF();
                String nationalite = in.readUTF();
                
                System.out.println(
                        "Demande vérification véhicule (" +
                        immatriculation + ")"
                );
                
                Object[] navire = this._data.emplacementVehicule(
                    immatriculation, nationalite
                );
                
                if (navire != null) {
                    System.out.println("Véhicule à replacer dans une file");
                    
                    out.writeChar('A');
                    out.writeUTF((String) navire[0]);
                    out.writeUTF((String) navire[1]);
                    out.flush();
                    
                    in.readChar();
                    
                    System.out.println("Véhicule replacé dans sa file");
                } else {
                    
                    System.out.println("Véhicule en situation illégale");
                    
                    out.writeChar('F');
                    out.flush();
                    
                    this.contactServicesDePolice(immatriculation, in, out);
                }
            } catch (Exception ex) {
                Logger.getLogger(MobileServerThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void contactServicesDePolice(String immatriculation, DataInputStream in, DataOutputStream out)
    {
        try {
            String emplacement = in.readUTF();
            String modele = in.readUTF();
            
            System.out.println(
                "Le véhicule immatriculé " + immatriculation +
                " (modèle " + modele +") se trouve en infraction à " +
                emplacement
            );
            
            //this.envoiPoliceEmail(immatriculation, emplacement, modele);
            
            System.out.println(
                "Services de police contactés"
            );
            
            in.readChar();
            
            System.out.println(
                "Services de police arrivés sur place"
            );
        } catch (IOException ex) {
            Logger.getLogger(MobileServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void envoiPoliceEmail(String immatriculation, String emplacement, String modele) {
        try {
            Properties property = System.getProperties();
            property.put("mail.smtp.host", Config.SMTP_SERVER);
            property.put("file.encoding", "iso-8859-1");
            Session smtpSession = Session.getDefaultInstance(property, null);
            
            MimeMessage msg = new MimeMessage(smtpSession);
            msg.setFrom(new InternetAddress(Config.EMAIL_SERVICE));
            msg.setRecipient(
                Message.RecipientType.TO,
                new InternetAddress(Config.EMAIL_POLICE)
            );
            
            msg.setSubject("Nouvelle constatation d'infraction");
            
            msg.setText(
                "Nous avons constatés un véhicule qui a tenté une intrusion.\n" +
                "Le véhicule est immatriculé " + immatriculation + " (modèle : "+
                modele +") et se trouve à " + emplacement
            );
            
            Transport.send(msg);
        } catch (MessagingException ex) {
            Logger.getLogger(MobileServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}