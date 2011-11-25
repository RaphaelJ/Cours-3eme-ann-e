/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

/**
 *
 * @author rapha
 */
public class VerifBooking implements Protocol {
    private int _code_reservation;
    private int _nbre_passagers;

    public VerifBooking(int _code_reservation, int _nbre_passagers) {
        this._code_reservation = _code_reservation;
        this._nbre_passagers = _nbre_passagers;
    }

    /**
     * @return the _code_reservation
     */
    public int getCode_reservation() {
        return _code_reservation;
    }

    /**
     * @param code_reservation the _code_reservation to set
     */
    public void setCode_reservation(int code_reservation) {
        this._code_reservation = code_reservation;
    }

    /**
     * @return the _nbre_passagers
     */
    public int getNbre_passagers() {
        return _nbre_passagers;
    }

    /**
     * @param nbre_passagers the _nbre_passagers to set
     */
    public void setNbre_passagers(int nbre_passagers) {
        this._nbre_passagers = nbre_passagers;
    }
}