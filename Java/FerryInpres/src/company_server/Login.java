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
    private String _name;
    private String _password;

    public Login(String _name, String _password) {
        this._name = _name;
        this._password = _password;
    }

    /**
     * @return the _nom
     */
    public String getName() {
        return _name;
    }

    /**
     * @param nom the _nom to set
     */
    public void setName(String name) {
        this._name = name;
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
