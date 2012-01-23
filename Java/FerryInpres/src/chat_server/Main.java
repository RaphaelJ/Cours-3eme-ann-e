/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_server;

import company_server.Config;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;

/**
 *
 * @author rapha
 */
public class Main {
    public static final int PORT_SERVEUR = 39020;
    public static final int PORT_CHAT = 39021;
    public static final String MYSQL_HOST = "127.0.0.1";
    
    public static final byte ACK = (byte) 'A';
    public static final byte FAIL = (byte) 'F';
    
    public static void main(String[] args)
            throws SQLException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, IOException
    {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        String url = "jdbc:mysql://"+Config.MYSQL_HOST+"/ferryinpres";
        Connection conn = DriverManager.getConnection(
            url, "ferryinpres", "pass"
        );
        
        ServerSocket server_sock = new ServerSocket(PORT_SERVEUR);
        
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            
            InputStream in = sock.getInputStream();
            OutputStream out = sock.getOutputStream();
            
            String user = readString(in);
            String pass = readString(in);
            
            if (agentValide(conn, user, pass)) {
                System.out.println(user+" connecté");
                out.write(ACK);
                out.write(String.valueOf(PORT_CHAT).getBytes());
                out.write('\0');
            } else {
                System.out.println(user+" n'a pas réussi son authentification");
                out.write(FAIL);
            }
            out.flush();
            
            sock.close();
        }
    }
    
    public static boolean agentValide(Connection conn, String user, String pass)
            throws SQLException
    {
        PreparedStatement instruc = conn.prepareStatement(
            "SELECT * FROM utilisateurs_chat WHERE user = ? AND pass = ?"
        );
        
        instruc.setString(1, user);
        instruc.setString(2, pass);
        ResultSet rs = instruc.executeQuery();
        
        return rs.next();
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
}
