/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

/**
 *
 * @author rapha
 */
public class Login implements Protocol {
    private String _nom;
    private String _password;

    public Login(String _nom, String _password) {
        this._nom = _nom;
        this._password = _password;
    }

    /**
     * @return the _nom
     */
    public String getNom() {
        return _nom;
    }

    /**
     * @param nom the _nom to set
     */
    public void setNom(String nom) {
        this._nom = nom;
    }

    /**
     * @return the _password
     */
    public String getPassword() {
        return _password;
    }

    /**
     * @param password the _password to set
     */
    public void setPassword(String password) {
        this._password = password;
    }
}
