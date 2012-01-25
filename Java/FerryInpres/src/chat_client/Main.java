/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chat_client;

import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author rapha
 */
public class Main {
    public static final int PORT_SERVEUR = 39020;
    public static final String HOTE_SERVEUR = "127.0.0.1";
    public static final String CHAT_GROUP = "224.0.0.1";
    
    public static final byte ACK = (byte) 'A';
    public static final byte FAIL = (byte) 'F';
    
    public static final byte QUESTION = (byte) 'Q';
    public static final byte ANSWER = (byte) 'A';
    public static final byte EVENT = (byte) 'E';
    
    public static void main(String[] args)
            throws UnknownHostException, IOException
    {
        Object[] ret = identificationAgent();
        
        chat((String) ret[0], (int) ret[1]);
    }
    
    public static Object[] identificationAgent()
            throws UnknownHostException, IOException
    {
        int port_chat;
        String user;
    
        do {
            Socket sock = new Socket(HOTE_SERVEUR, PORT_SERVEUR);

            OutputStream out = sock.getOutputStream();
            InputStream in = sock.getInputStream();
            
            System.out.println("Nom agent: ");
            user = readLine();
            System.out.println("Mot de passe: ");
            String pass = readLine();
            
            out.write(String.valueOf(user).getBytes());
            out.write('\0');
            out.write(String.valueOf(pass).getBytes());
            out.write('\0');
            out.flush();
            
            if ((byte) in.read() == ACK) {
                System.out.println("Identification réussie");
                port_chat = Integer.parseInt(readString(in));
                System.out.println("Port du chat: "+port_chat);
            } else {
                System.out.println("Mauvais identifiants");
                port_chat = -1;
            }
        } while (port_chat == -1);
        
        return new Object[] { user, port_chat };
    }
    
    public static void chat(String user, int port_chat) throws IOException
    {
        InetAddress group = InetAddress.getByName(CHAT_GROUP);
        final MulticastSocket sock = new MulticastSocket(port_chat);
        sock.joinGroup(group);
        
        String help =
            "Précédez votre message par Q pour une question, par A suivi de " +
            "l'identifiant d'une question pour une réponse et E pour un " +
            "événement.";
        
        System.out.println(help);
        
        // Lance un thread pour afficher les messages reçus
        (new Thread() {
            public void run() 
            {
                receptionMessages(sock);
            }
        }).start();
        
        for (;;) {
            System.out.print("> ");
            String message = readLine();
            
            if (message.length() == 0)
                continue;
            
            ByteArrayOutputStream out = new ByteArrayOutputStream(1000);
            
            switch (message.charAt(0)) {
            case 'Q':
                out.write(QUESTION);
                out.write(user.getBytes());
                out.write('\0');
                out.write(message.substring(2).getBytes());
                out.write('\0');
                break;
            case 'A':
                out.write(ANSWER);
                out.write(user.getBytes());
                out.write('\0');
                
                int posFinId = message.indexOf(' ', 2);
                out.write(message.substring(2, posFinId).getBytes());
                out.write('\0');
                
                out.write(message.substring(posFinId + 1).getBytes());
                out.write('\0');
                break;
            case 'E':
                out.write(EVENT);
                out.write(user.getBytes());
                out.write('\0');
                out.write(message.substring(2).getBytes());
                out.write('\0');
                break;
            default:
                System.out.println("Message non valide.");
                System.out.println(help);
                continue;
            }
            
            DatagramPacket packet = new DatagramPacket(
                out.toByteArray(), out.size(), group, port_chat
            );
            sock.send(packet);
        }
    }
    
    public static void receptionMessages(MulticastSocket sock)
    {
        for (;;) {
            try {
                byte[] buf = new byte[1000];
                DatagramPacket recv = new DatagramPacket(buf, buf.length);
                sock.receive(recv);

                ByteArrayInputStream in = new ByteArrayInputStream(
                    buf, 1, 1000
                );
                String user = readString(in);
                
                System.out.println();

                switch (buf[0]) {
                case 'Q':
                    String question = readString(in);
                    System.out.println(
                        "<"+user+"> Question #"+Math.abs(question.hashCode())+": "+
                        question
                    );
                    break;
                case 'A':
                    String idQuestion = readString(in);
                    String reponse = readString(in);
                    System.out.println(
                        "<"+user+"> Réponse à la question #"+idQuestion+": "+
                        reponse
                    );
                    break;
                case 'E':
                    String evenement = readString(in);
                    System.out.println(
                        "<"+user+"> Evenement: "+evenement
                    );
                    break;
                default:    
                }
            } catch (Exception ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
}
