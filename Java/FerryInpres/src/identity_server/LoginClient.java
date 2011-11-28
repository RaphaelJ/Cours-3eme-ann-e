/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package identity_server;

/**
 *
 * @author rapha
 */
public class LoginClient implements Protocol {
    private String _name;
    private byte[] _password_hashed;
    private int _client_salt;
    
    public LoginClient(String _name, byte[] _password_hashed, int _client_salt) {
        this._name = _name;
        this._password_hashed = _password_hashed;
        this._client_salt = _client_salt;
    }

    /**
     * @return the _name
     */
    public String getName() {
        return _name;
    }

    /**
     * @param name the _name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the _password_hashed
     */
    public byte[] getPassword_hashed() {
        return _password_hashed;
    }

    /**
     * @param password_hashed the _password_hashed to set
     */
    public void setPassword_hashed(byte[] password_hashed) {
        this._password_hashed = password_hashed;
    }

    /**
     * @return the _client_hash
     */
    public int getClient_salt() {
        return _client_salt;
    }

    /**
     * @param client_hash the _client_hash to set
     */
    public void setClient_salt(int client_salt) {
        this._client_salt = client_salt;
    }
}
