/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

import java.util.TreeMap;

/**
 *
 * @author rapha
 */
public class MonnaiesResponseProtocol {
    private TreeMap<String, Double> _monnaies;
    
    /**
     * @return the _monnaies
     */
    public TreeMap<String, Double> getMonnaies() {
        return _monnaies;
    }

    /**
     * @param monnaies the _monnaies to set
     */
    public void setMonnaies(TreeMap<String, Double> monnaies) {
        this._monnaies = monnaies;
    }
}
