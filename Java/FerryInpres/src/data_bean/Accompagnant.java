/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Accompagnant {
    private int _id;
    private String _nom;
    private String _prenom;
    private String _adresse;
    private int _voyageur;

    public Accompagnant(int _id, String _nom, String _prenom, String _adresse,
            int _voyageur) {
        this._id = _id;
        this._nom = _nom;
        this._prenom = _prenom;
        this._adresse = _adresse;
        this._voyageur = _voyageur;
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
     * @return the _nom
     */
    public String getNom() {
        return _nom;
    }

    /**
     * @param nom the _nom to set
     */
    public void setNom(String nom) {
        this._nom = nom;
    }

    /**
     * @return the _prenom
     */
    public String getPrenom() {
        return _prenom;
    }

    /**
     * @param prenom the _prenom to set
     */
    public void setPrenom(String prenom) {
        this._prenom = prenom;
    }

    /**
     * @return the _adresse
     */
    public String getAdresse() {
        return _adresse;
    }

    /**
     * @param adresse the _adresse to set
     */
    public void setAdresse(String adresse) {
        this._adresse = adresse;
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
