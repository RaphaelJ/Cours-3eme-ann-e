/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

/**
 *
 * @author rapha
 */
public class KeyExchangeServer implements Protocol {
    private byte[] cryptedSessionKey;
    
    public KeyExchangeServer(byte[] cryptedSessionKey) {
        this.cryptedSessionKey = cryptedSessionKey;
    }

    /**
     * @return the cryptedSessionKey
     */
    public byte[] getCryptedSessionKey() {
        return cryptedSessionKey;
    }

    /**
     * @param cryptedSessionKey the cryptedSessionKey to set
     */
    public void setCryptedSessionKey(byte[] cryptedSessionKey) {
        this.cryptedSessionKey = cryptedSessionKey;
    }
    
}
