/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Voyageur {
    private int _id;
    private String _nom;
    private String _prenom;
    private String _adresse;
    private String _email;
    
    public Voyageur(int id, String nom, String prenom, String adresse,
                    String email) {
        this._id = id;
        this._nom = nom;
        this._prenom = prenom;
        this._adresse = adresse;
        this._email = email;
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
     * @return the _email
     */
    public String getEmail() {
        return _email;
    }

    /**
     * @param email the _email to set
     */
    public void setEmail(String email) {
        this._email = email;
    }
    
    
}
