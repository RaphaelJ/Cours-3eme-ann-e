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
public class MeteoQueryProtocol implements Serializable {
    private int[] _jours;

    public MeteoQueryProtocol(int[] jours) {
        this._jours = jours;
    }

    /**
     * @return the _jours
     */
    public int[] getJours() {
        return _jours;
    }

    /**
     * @param jours the _jours to set
     */
    public void setJours(int[] jours) {
        this._jours = jours;
    }
    
}
