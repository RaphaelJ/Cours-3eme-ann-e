/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package FreeTax;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author rapha
 */
public class Achats {
    public static void gererAchats(int code_reserv, HttpServletRequest request) 
            throws Exception
    {
        String str_ajout = request.getParameter("ajout");
        String str_enlever = request.getParameter("enlever");
        
        System.out.print(str_ajout);
        
        if (str_ajout != null) {
            int achat = Integer.parseInt(str_ajout);
            if (Database.produitDispo(achat)) {
                Database.ajoutProduit(code_reserv, achat);
            }
        } else if (str_enlever != null) {
            Database.enleverProduit(code_reserv, Integer.parseInt(str_enlever));
        }
    }
    
    public static boolean gererCommande(int code_reserv, HttpServletRequest request) 
            throws Exception
    {
        String str_carte = request.getParameter("carte");
        
        if (str_carte != null) {
            int carte = Integer.parseInt(str_carte);
            Socket sock = new Socket("127.0.0.1", 39020);
            
            DataInputStream in = new DataInputStream(sock.getInputStream());
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            
            out.writeInt(carte);
            out.flush();
            
            if (in.readBoolean()) { // Carte valide
                Database.commander(code_reserv);
                return true;
            }
        }
        
        return false;
    }
}
