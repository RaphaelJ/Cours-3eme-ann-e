/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Reservation {
    private int _id;
    private boolean _paye;
    private boolean _checkin;
    private int _traversee;
    private int _voyageur;

    public Reservation(int id, boolean paye, boolean checkin, int traversee,
                       int voyageur) {
        this._id = id;
        this._paye = paye;
        this._checkin = checkin;
        this._traversee = traversee;
        this._voyageur = voyageur;
    }

    /**
     * @return the _id
     */
    public int getId() {
        return _id;
    }

    /**
     * @param id the _id to set
     */
    public void setId(int id) {
        this._id = id;
    }

    /**
     * @return the _paye
     */
    public boolean isPaye() {
        return _paye;
    }

    /**
     * @param paye the _paye to set
     */
    public void setPaye(boolean paye) {
        this._paye = paye;
    }

    /**
     * @return the _checkin
     */
    public boolean isCheckin() {
        return _checkin;
    }

    /**
     * @param checkin the _checkin to set
     */
    public void setCheckin(boolean checkin) {
        this._checkin = checkin;
    }

    /**
     * @return the _traversee
     */
    public int getTraversee() {
        return _traversee;
    }

    /**
     * @param traversee the _traversee to set
     */
    public void setTraversee(int traversee) {
        this._traversee = traversee;
    }

    /**
     * @return the _voyageur
     */
    public int getVoyageur() {
        return _voyageur;
    }

    /**
     * @param voyageur the _voyageur to set
     */
    public void setVoyageur(int voyageur) {
        this._voyageur = voyageur;
    }    
}
