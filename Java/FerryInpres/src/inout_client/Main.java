/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package inout_client;

import java.io.*;
import java.net.*;
import java.util.*;

import status_client.StatusClient;
        
/**
 *
 * @author rapha
 */
public class Main {
    public static final byte SHIP_IN = (byte) 'I';
    public static final byte SHIP_OUT = (byte) 'O';
    
    public static void main(String[] args) throws IOException
    {        
        Properties prop = new Properties();
        prop.load(new FileInputStream("inout_client.properties"));
                
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
        
        int choix;
        do {
            System.out.println("1. Un ferry vient d'arriver");
            System.out.println("2. Un ferry vient de partir");
            System.out.println("3. Quitter");
            
            System.out.println("Votre choix: ");
            choix = readInt();
            
            if (choix == 1)
                Main.ferryIn(in, out);
            else if (choix == 2)
                Main.ferryOut(in, out);
        } while (choix != 3);
        
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
    
    // Donne l'heure sous forme de chaine de caractères
    public static String currentTime() 
    {
        java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("HH:mm");
        return f.format(new Date());
    }
    
    public static void ferryIn(InputStream in, OutputStream out)
            throws IOException
    {
        System.out.println("Numero du ferry entrant: ");
        int ferry_id = readInt();
        
        out.write(SHIP_IN);
        out.write(String.valueOf(ferry_id).getBytes());
        out.write('\0');
        out.write(currentTime().getBytes());
        out.write('\0');
        out.flush();
    }
    
    public static void ferryOut(InputStream in, OutputStream out)
            throws IOException
    {
        System.out.println("Numero du ferry sortant: ");
        int ferry_id = readInt();
        
        out.write(SHIP_OUT);
        out.write(String.valueOf(ferry_id).getBytes());
        out.write('\0');
        out.write(currentTime().getBytes());
        out.write('\0');
        out.flush();
    }
}
