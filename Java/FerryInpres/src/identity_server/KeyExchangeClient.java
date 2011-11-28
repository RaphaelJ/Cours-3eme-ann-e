/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

import java.security.PublicKey;

/**
 *
 * @author rapha
 */
public class KeyExchangeClient implements Protocol {
    private PublicKey publicKey;
    
    public KeyExchangeClient(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
