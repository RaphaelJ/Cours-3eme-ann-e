/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.io.Serializable;

/**
 *
 * @author rapha
 */
public class MonnaiesQueryProtocol implements Serializable {
    private String[] _monnaies;

    public MonnaiesQueryProtocol(String[] monnaies) {
        this._monnaies = monnaies;
    }

    /**
     * @return the _monnaies
     */
    public String[] getMonnaies() {
        return _monnaies;
    }

    /**
     * @param monnaies the _monnaies to set
     */
    public void setMonnaies(String[] monnaies) {
        this._monnaies = monnaies;
    }
    
}
