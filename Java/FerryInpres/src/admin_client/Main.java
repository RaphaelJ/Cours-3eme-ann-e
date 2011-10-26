/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_client;

import java.io.*;
import java.net.*;
import java.util.*;

import status_client.StatusClient;
        
/**
 *
 * @author rapha
 */
public class Main {
    public static final byte ACK = (byte) 'A';
    public static final byte FAIL = (byte) 'F';
    public static final byte LOGIN = (byte) 'L';
    public static final byte LCLIENTS = (byte) 'C';
    public static final byte PAUSE = (byte) 'P';
    public static final byte STOP = (byte) 'S';
    
    public static void main(String[] args) throws IOException
    {        
        Properties prop = new Properties();
        prop.load(new FileInputStream("admin_client.properties"));
        
        String ip = prop.getProperty("server_ip");
        int server_port = Integer.parseInt(prop.getProperty("server_port"));
        int status_server_port = Integer.parseInt(
            prop.getProperty("status_server_port")
        );
        
        StatusClient status_client = new StatusClient(ip, status_server_port);
        status_client.start();
        
        Socket sock = new Socket(ip, server_port);
        InputStream in = sock.getInputStream();
        OutputStream out = sock.getOutputStream();
        
        if (adminLogin(in, out)) {
            int choix;
            do {
                System.out.println("1. Lister les clients connectes");
                System.out.println("2. Mettre le serveur en pause");
                System.out.println("3. Arreter le serveur");
                System.out.println("4. Quitter");
            
                System.out.println("Votre choix: ");
                choix = readInt();
            
                if (choix == 1)
                    listClients(in, out);
                else if (choix == 2)
                    pauseServer(in, out);
                else if (choix == 3)
                    stopServer(in, out);
            } while (choix != 4 && choix != 3);
        }
        
        sock.close();
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
    
    // Envoie les informations de connexion au serveur. Retourne true si
    // authentifié
    public static boolean adminLogin(InputStream in, OutputStream out)
            throws IOException
    {
        System.out.println("Nom d'utilisateur: ");
        String user = readLine();
        System.out.println("Mot de passe: ");
        String pass = readLine();
        
        out.write(LOGIN);
        out.write(String.valueOf(user).getBytes());
        out.write('\0');
        out.write(String.valueOf(pass).getBytes());
        out.write('\0');
        out.flush();
        
        if ((byte) in.read() == ACK) {
            System.out.println("Connexion réussie");
            return true;
        } else {
            System.out.println("Mauvais identifiants");
            return false;
        }
        
    }
    
    public static void listClients(InputStream in, OutputStream out)
            throws IOException
    {        
        out.write(LCLIENTS);
        out.flush();
        
        int n_clients = Integer.parseInt(readString(in));
        System.out.println(n_clients + " clients connectés :");
        for (int i = 0; i < n_clients; i++) {
            int terminal_id = Integer.parseInt(readString(in));
            System.out.println(
               "    Terminal: " + terminal_id
            );    
        }
    }
    
    public static void pauseServer(InputStream in, OutputStream out)
            throws IOException
    {
        System.out.println("Le serveur va être en pause pour 20 secondes");
        
        out.write(PAUSE);
        out.flush();
    }
    
    public static void stopServer(InputStream in, OutputStream out)
            throws IOException
    {
        System.out.println("Nombre de secondes avant l'arrêt du serveur: ");
        int secs = readInt();
        
        out.write(STOP);
        out.write(String.valueOf(secs).getBytes());
        out.write('\0');
        out.flush();
    }
}