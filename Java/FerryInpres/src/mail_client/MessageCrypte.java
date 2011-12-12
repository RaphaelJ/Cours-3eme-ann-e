/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mail_client;

import java.io.Serializable;

/**
 *
 * @author rapha
 */
public class MessageCrypte implements Serializable {
    private String _message;
    private int _hash;

    public MessageCrypte(String _message, int _hash) {
        this._message = _message;
        this._hash = _hash;
    }

    /**
     * @return the _message
     */
    public String getMessage() {
        return _message;
    }

    /**
     * @param message the _message to set
     */
    public void setMessage(String message) {
        this._message = message;
    }

    /**
     * @return the _hash
     */
    public int getHash() {
        return _hash;
    }

    /**
     * @param hash the _hash to set
     */
    public void setHash(int hash) {
        this._hash = hash;
    }
    
}
