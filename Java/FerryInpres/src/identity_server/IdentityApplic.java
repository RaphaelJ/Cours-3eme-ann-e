/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

    
    // Lit un entier depuis l'entrée standard
/**
 *
 * @author rapha
 */
public class IdentityApplic {
    public static void main(String[] args)
            throws UnknownHostException, IOException, ClassNotFoundException,
            NoSuchAlgorithmException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException, InvalidKeyException
    {
        Socket sock = new Socket(Config.IDENTITY_SERVER, Config.IDENTITY_PORT);
        ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
        
        SecretKey sessionKey = keyExchange(in, out);
        
        // Crée les instances de cryptage et de décryptage
        Cipher cryptor = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cryptor.init(Cipher.ENCRYPT_MODE, sessionKey);
        Cipher decryptor = Cipher.getInstance("DES/ECB/PKCS5Padding");
        decryptor.init(Cipher.DECRYPT_MODE, sessionKey);
        
        login(in, out, cryptor, decryptor);
    }
    
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
    
    public static SecretKey keyExchange(ObjectInputStream in,
            ObjectOutputStream out)
            throws IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, NoSuchAlgorithmException, IOException,
            ClassNotFoundException, NoSuchPaddingException
    {
        // Génère une paire de clé pour la réception de la clé de cryptage
        // asynchrone
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, new SecureRandom());
        KeyPair keys = gen.generateKeyPair();
        PublicKey publicKey = keys.getPublic();
        PrivateKey privateKey = keys.getPrivate();
        
        // Envoie la clé publique au serveur
        out.writeObject(new KeyExchangeClient(publicKey));
        out.flush();
        
        // Recoit la clé de session cryptée par le serveur
        KeyExchangeServer response = (KeyExchangeServer) in.readObject();
        
        // Décrypte la clé de session avec la clé privée
        Cipher decryptor = Cipher.getInstance("RSA/ECB/PKCS#1");
        decryptor.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] sessionKeyEncoded =
                decryptor.doFinal(response.getCryptedSessionKey());
        return new SecretKeySpec(sessionKeyEncoded, "DES");
        
    }

    private static void login(ObjectInputStream in, ObjectOutputStream out,
            Cipher cryptor, Cipher decryptor)
            throws IOException, ClassNotFoundException, 
            IllegalBlockSizeException, BadPaddingException,
            NoSuchAlgorithmException
    {
        // Reçoi et décrypte le sel de hashage du serveur
        LoginServer saltQuery = (LoginServer) Utils.decryptObject(
                (byte[]) in.readObject(), decryptor
        );
        
        System.out.println("Nom d'utilisateur: ");
        String user = readLine();
        System.out.println("Mot de passe: ");
        String pass = readLine();
        
        int clientSalt = (new Random()).nextInt();
        
        out.writeObject(
            Utils.cryptObject(
                new LoginClient(
                    user, Utils.hashPassword(
                        pass, clientSalt, saltQuery.getHashSalt()
                    ), clientSalt
                ), cryptor
            )
        );
        out.flush();
    }
        
        
        
        
        System.out.println("Nom d'utilisateur: ");
        String user = readLine();
        System.out.println("Mot de passe: ");
        String pass = readLine();
        
        out.writeObject(new LoginServer(user, pass));
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
            System.out.println("Départ: " + abt.getDate_depart());
            System.out.println("Ferry: " + abt.getNom_ferry());
            System.out.println("Numero de client: " + abt.getNum_client());
        } else if (response instanceof Fail) {
            System.out.println(
               "Il n'y a pas de possibilité de départ"
            );
        }
    }
}