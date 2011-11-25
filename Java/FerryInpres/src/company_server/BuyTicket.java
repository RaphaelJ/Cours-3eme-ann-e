/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package company_server;

/**
 *
 * @author rapha
 */
public class BuyTicket implements Protocol {
    private String _nom_conducteur;
    private String _immatriculation;
    private int _nbre_passagers;

    public BuyTicket(String _nom_conducteur, String _immatriculation, int _nbre_passagers) {
        this._nom_conducteur = _nom_conducteur;
        this._immatriculation = _immatriculation;
        this._nbre_passagers = _nbre_passagers;
    }

    /**
     * @return the _nom_conducteur
     */
    public String getNom_conducteur() {
        return _nom_conducteur;
    }

    /**
     * @param nom_conducteur the _nom_conducteur to set
     */
    public void setNom_conducteur(String nom_conducteur) {
        this._nom_conducteur = nom_conducteur;
    }

    /**
     * @return the _immatriculation
     */
    public String getImmatriculation() {
        return _immatriculation;
    }

    /**
     * @param immatriculation the _immatriculation to set
     */
    public void setImmatriculation(String immatriculation) {
        this._immatriculation = immatriculation;
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
