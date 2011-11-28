/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 *
 * @author rapha
 */
public class IdentityServer {
    public static void main(String[] args)
            throws IOException, ClassNotFoundException, InstantiationException,
            IllegalAccessException, SQLException
    {
        ServerSocket server_sock = new ServerSocket(Config.IDENTITY_PORT);
        
        ExecutorService pool = Executors.newFixedThreadPool(12);
                
        for (;;) {
            System.out.println("En attente d'un nouveau client");
            Socket sock = server_sock.accept();
            System.out.println("Nouveau client");
            
            pool.execute(new ServerThread(sock));
        }
    }
}
class ServerThread implements Runnable {
    private Socket _sock;
    
    public ServerThread(Socket sock)
    {
        this._sock = sock;
    }
    
    @Override
    public void run()
    {
        try {
            ObjectInputStream in = new ObjectInputStream(
                this._sock.getInputStream()
            );
            ObjectOutputStream out = new ObjectOutputStream(
                this._sock.getOutputStream()
            );
            
            SecretKey sessionKey = this.keyExchange(in, out);
            
            
            
            
        } catch (Exception e) {
            System.err.println("Fermeture de la connexion: ");
            e.printStackTrace();
        }
        
        try {
            this._sock.close();
        } catch (IOException e) { }
    }
    
    public SecretKey keyExchange(ObjectInputStream in, ObjectOutputStream out)
            throws IOException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            ClassNotFoundException
    {
        // Recoit la clé publique
        KeyExchangeClient query = (KeyExchangeClient) in.readObject();
            
        // Génère la clé de session
        KeyGenerator gen = KeyGenerator.getInstance("DES");
        gen.init(new SecureRandom());
        SecretKey sessionKey = gen.generateKey();
            
        // Envoie le clé de session
        Cipher rsaCryptor = Cipher.getInstance("RSA/ECB/PKCS#1");
        rsaCryptor.init(Cipher.ENCRYPT_MODE, query.getPublicKey());
        out.writeObject(sessionKey.getEncoded());
        out.flush();
        
        return sessionKey;
    }
}
