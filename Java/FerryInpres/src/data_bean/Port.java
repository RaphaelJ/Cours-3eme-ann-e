/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package data_bean;

/**
 *
 * @author rapha
 */
public class Port {
    private String _nom;
    private int _nombre_terminaux;
    private int _nombre_terminaux_occuppes;
    private String _pays;
    
    public Port(String nom, int nombre_terminaux, int nombre_terminaux_occupes,
                String pays)
    {
        this._nom = nom;
        this._nombre_terminaux = nombre_terminaux;
        this._nombre_terminaux_occuppes = nombre_terminaux_occupes;
        this._pays = pays;
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
     * @return the _nombre_terminaux
     */
    public int getNombreTerminaux() {
        return _nombre_terminaux;
    }

    /**
     * @param nombre_terminaux the _nombre_terminaux to set
     */
    public void setNombreTerminaux(int nombre_terminaux) {
        this._nombre_terminaux = nombre_terminaux;
    }

    /**
     * @return the _nombre_terminaux_occuppes
     */
    public int getNombreTerminauxOccuppes() {
        return _nombre_terminaux_occuppes;
    }

    /**
     * @param nombre_terminaux_occuppes the _nombre_terminaux_occuppes to set
     */
    public void setNombreTerminauxOccuppes(int nombre_terminaux_occuppes) {
        this._nombre_terminaux_occuppes = nombre_terminaux_occuppes;
    }

    /**
     * @return the _pays
     */
    public String getPays() {
        return _pays;
    }

    /**
     * @param pays the _pays to set
     */
    public void setPays(String pays) {
        this._pays = pays;
    }
}