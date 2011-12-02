/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

/**
 *
 * @author rapha
 */
public class FreetaxStatsLogin implements FreetaxStatsProtocol {
    private String _login;
    private String _hashedPassword;

    public FreetaxStatsLogin(String _login, String _hashedPassword) {
        this._login = _login;
        this._hashedPassword = _hashedPassword;
    }

    /**
     * @return the _login
     */
    public String getLogin() {
        return _login;
    }

    /**
     * @param login the _login to set
     */
    public void setLogin(String login) {
        this._login = login;
    }

    /**
     * @return the _hashedPassword
     */
    public String getHashedPassword() {
        return _hashedPassword;
    }

    /**
     * @param hashedPassword the _hashedPassword to set
     */
    public void setHashedPassword(String hashedPassword) {
        this._hashedPassword = hashedPassword;
    }
    
    
}
