/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package information_server;

/**
 *
 * @author rapha
 */
public class FreetaxStatsDescReponse implements FreetaxStatsProtocol {
    private int[] _ventes;
    private double _moyenne;
    private int _mode;
    private double _ecartType;
    
    public FreetaxStatsDescReponse(int[] _ventes, double _moyenne, int _mode, double _ecartType) {
        this._ventes = _ventes;
        this._moyenne = _moyenne;
        this._mode = _mode;
        this._ecartType = _ecartType;
    }

    /**
     * @return the _ventes
     */
    public int[] getVentes() {
        return _ventes;
    }

    /**
     * @param ventes the _ventes to set
     */
    public void setVentes(int[] ventes) {
        this._ventes = ventes;
    }

    /**
     * @return the _moyenne
     */
    public double getMoyenne() {
        return _moyenne;
    }

    /**
     * @param moyenne the _moyenne to set
     */
    public void setMoyenne(double moyenne) {
        this._moyenne = moyenne;
    }

    /**
     * @return the _mode
     */
    public int getMode() {
        return _mode;
    }

    /**
     * @param mode the _mode to set
     */
    public void setMode(int mode) {
        this._mode = mode;
    }

    /**
     * @return the _ecartType
     */
    public double getEcartType() {
        return _ecartType;
    }

    /**
     * @param ecartType the _ecartType to set
     */
    public void setEcartType(double ecartType) {
        this._ecartType = ecartType;
    }
}
