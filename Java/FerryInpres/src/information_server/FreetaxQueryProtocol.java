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
public class FreetaxQueryProtocol implements Serializable {
    private boolean _alcools;
    private boolean _parfums;
    private boolean _tabacs;

    public FreetaxQueryProtocol(boolean alcools, boolean parfums,
            boolean tabacs) {
        this._alcools = alcools;
        this._parfums = parfums;
        this._tabacs = tabacs;
    }

    /**
     * @return the _alcools
     */
    public boolean isAlcools() {
        return _alcools;
    }

    /**
     * @param alcools the _alcools to set
     */
    public void setAlcools(boolean alcools) {
        this._alcools = alcools;
    }

    /**
     * @return the _parfums
     */
    public boolean isParfums() {
        return _parfums;
    }

    /**
     * @param parfums the _parfums to set
     */
    public void setParfums(boolean parfums) {
        this._parfums = parfums;
    }

    /**
     * @return the _tabacs
     */
    public boolean isTabacs() {
        return _tabacs;
    }

    /**
     * @param tabacs the _tabacs to set
     */
    public void setTabacs(boolean tabacs) {
        this._tabacs = tabacs;
    }
}
