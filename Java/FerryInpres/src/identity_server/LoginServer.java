/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

/**
 *
 * @author rapha
 */
public class LoginServer implements Protocol {
    private int hashSalt;
    
    public LoginServer(int hashSalt) {
        this.hashSalt = hashSalt;
    }

    /**
     * @return the hashSalt
     */
    public int getHashSalt() {
        return hashSalt;
    }

    /**
     * @param hashSalt the hashSalt to set
     */
    public void setHashSalt(int hashSalt) {
        this.hashSalt = hashSalt;
    }
}
