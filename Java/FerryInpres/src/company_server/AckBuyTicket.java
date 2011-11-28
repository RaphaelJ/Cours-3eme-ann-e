/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

import java.sql.Timestamp;

/**
 *
 * @author rapha
 */
public class AckBuyTicket implements Protocol {
    private String _nom_ferry;
    private Timestamp _date_depart;
    private int _num_client;

    public AckBuyTicket(String _nom_ferry, Timestamp _date_depart, int _num_client) {
        this._nom_ferry = _nom_ferry;
        this._date_depart = _date_depart;
        this._num_client = _num_client;
    }

    /**
     * @return the _nom_ferry
     */
    public String getNom_ferry() {
        return _nom_ferry;
    }

    /**
     * @param nom_ferry the _nom_ferry to set
     */
    public void setNom_ferry(String nom_ferry) {
        this._nom_ferry = nom_ferry;
    }

    /**
     * @return the _date_depart
     */
    public Timestamp getDate_depart() {
        return _date_depart;
    }

    /**
     * @param date_depart the _date_depart to set
     */
    public void setDate_depart(Timestamp date_depart) {
        this._date_depart = date_depart;
    }

    /**
     * @return the _num_client
     */
    public int getNum_client() {
        return _num_client;
    }

    /**
     * @param num_client the _num_client to set
     */
    public void setNum_client(int num_client) {
        this._num_client = num_client;
    }
}
