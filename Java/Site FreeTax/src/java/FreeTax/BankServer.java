/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FreeTax;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

/**
 *
 * @author rapha
 */
public class BankServer {
    public static void main(String[] args) throws Exception
    {
        Properties prop = new Properties();
        prop.load(new FileInputStream("ferryinpres.properties"));
        
        ServerSocket server_sock = new ServerSocket(Integer.parseInt(prop.getProperty("BANK_PORT")));
        
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            System.out.println("Nouveau client");
            
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            
            out.writeBoolean(Database.compteValide(in.readInt()));
            
            in.close();
            out.close();
            sock.close();
        }
    }
}
