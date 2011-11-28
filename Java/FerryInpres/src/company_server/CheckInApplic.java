/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author rapha
 */
public class CheckInApplic {
    public static void main(String[] args)
            throws UnknownHostException, IOException, ClassNotFoundException
    {
        Socket sock = new Socket(Config.COMPANY_SERVER, Config.COMPANY_PORT);
        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
        
        System.out.println("Nom d'utilisateur: ");
        String user = readLine();
        System.out.println("Mot de passe: ");
        String pass = readLine();
        
        out.writeObject(new Login(user, pass));
        out.flush();
        
        Protocol response = (Protocol) in.readObject();
        if (response instanceof Ack) {
            int choix;
            do {
                System.out.println("1. Valider une réservation");
                System.out.println("2. Acheter une traversée");
                System.out.println("3. Quitter");

                System.out.println("Votre choix: ");
                choix = readInt();

                if (choix == 1)
                    verifBooking(in, out);
                else if (choix == 2)
                    buyTicket(in, out);
            } while (choix != 3);

            sock.close();
        } else {
            System.out.println("Identifiants invalides");
        }
    }
    
    // Lit un entier depuis l'entrée standard
    public static int readInt() throws IOException
    {
        BufferedReader inStream = new BufferedReader (
            new InputStreamReader(System.in)
        );
        return Integer.parseInt(inStream.readLine()); 
    }
    
    // Lit une lige depuis l'entrée standard
    public static String readLine() throws IOException
    {
        BufferedReader inStream = new BufferedReader (
            new InputStreamReader(System.in)
        );
        return inStream.readLine();
    }
    
    // Lit une chaine de caractères terminant avec \0 depuis un stream
    public static String readString(InputStream in) throws IOException
    {
        byte c;
        StringBuilder buffer = new StringBuilder();
        while ((c = (byte) in.read()) != '\0') {
            buffer.append((char) c);
        }
    
        return buffer.toString();
    }
    
    private static void verifBooking(ObjectInputStream in,
            ObjectOutputStream out) throws IOException, ClassNotFoundException
    {
        System.out.println("Entrez votre code de réservation: ");
        int reservation = readInt();
        System.out.println("Entrez le nombre de passagers: ");
        int n_passagers = readInt();
                
        out.writeObject(new VerifBooking(reservation, n_passagers));
        out.flush();
        
        Protocol response = (Protocol) in.readObject();
        if (response instanceof Ack) {
            System.out.println("Votre checkin a été validé");
        } else if (response instanceof Fail) {
            System.out.println(
               "Mauvais code de réservation ou réservation déjà validée"
            );
        }
    }
    
    private static void buyTicket(ObjectInputStream in,
            ObjectOutputStream out) throws IOException, ClassNotFoundException
    {
        System.out.println("Nom du conducteur: ");
        String conducteur = readLine();
        System.out.println("Numero d'immatriculation: ");
        String immatriculation = readLine();
        System.out.println("Nombre de passagers: ");
        int passagers = readInt();
        
        out.writeObject(new BuyTicket(conducteur, immatriculation, passagers));
        out.flush();
        
        Protocol response = (Protocol) in.readObject();
        if (response instanceof AckBuyTicket) {
            AckBuyTicket abt = (AckBuyTicket) response;
            System.out.println("Votre checkin a été validé");
        } else if (response instanceof Fail) {
            System.out.println(
               "Il n'y a pas de possibilité de départ"
            );
        }
    }
}