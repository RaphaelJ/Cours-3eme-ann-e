/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

import java.sql.Timestamp;

/**
 *
 * @author rapha
 */
public class Traversee {
    private int _id;
    private Timestamp _date_depart;
    private int _navire;
    private String _port_depart;
    private String _port_arrivee;
    
    public Traversee(int id, Timestamp date_depart, int navire,
                     String port_depart, String port_arrivee) {
        this._id = id;
        this._date_depart = date_depart;
        this._navire = navire;
        this._port_depart = port_depart;
        this._port_arrivee = port_arrivee;
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
     * @return the _date_depart
     */
    public Timestamp getDateDepart() {
        return _date_depart;
    }

    /**
     * @param date_depart the _date_depart to set
     */
    public void setDateDepart(Timestamp date_depart) {
        this._date_depart = date_depart;
    }

    /**
     * @return the _navire
     */
    public int getNavire() {
        return _navire;
    }

    /**
     * @param navire the _navire to set
     */
    public void setNavire(int navire) {
        this._navire = navire;
    }

    /**
     * @return the _port_depart
     */
    public String getPortDepart() {
        return _port_depart;
    }

    /**
     * @param port_depart the _port_depart to set
     */
    public void setPortDepart(String port_depart) {
        this._port_depart = port_depart;
    }

    /**
     * @return the _port_arrivee
     */
    public String getPortArrivee() {
        return _port_arrivee;
    }

    /**
     * @param port_arrivee the _port_arrivee to set
     */
    public void setPortArrivee(String port_arrivee) {
        this._port_arrivee = port_arrivee;
    }
}