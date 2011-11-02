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
    private Integer[] _jours;

    public MeteoQueryProtocol(Integer[] jours) {
        this._jours = jours;
    }

    /**
     * @return the _jours
     */
    public Integer[] getJours() {
        return _jours;
    }

    /**
     * @param jours the _jours to set
     */
    public void setJours(Integer[] jours) {
        this._jours = jours;
    }
    
}
